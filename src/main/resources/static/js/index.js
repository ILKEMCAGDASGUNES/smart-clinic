import { login } from "./services/authService.js";

const roleSelect = document.getElementById("roleSelect");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const loginBtn = document.getElementById("loginBtn");
const errorEl = document.getElementById("error");

// Eğer token + role varsa direkt dashboard’a gönder
const existingToken = localStorage.getItem("token");
const existingRole = localStorage.getItem("role");
if (existingToken && existingRole) {
    window.location.href = existingRole === "ADMIN" ? "/admin/dashboard" : "/doctor/dashboard";
}

loginBtn.addEventListener("click", async () => {
    errorEl.textContent = "";

    const role = roleSelect.value;
    const email = emailInput.value.trim();
    const password = passwordInput.value;

    if (!email || !password) {
        errorEl.textContent = "Email ve password zorunlu.";
        return;
    }

    try {
        const result = await login(email, password);

        // Beklediğimiz response örnekleri:
        // 1) { token: "...", role: "ADMIN" }
        // 2) { jwt: "...", roles: ["ADMIN"] }
        const token = result.token || result.jwt || result.accessToken;
        const serverRole = result.role || (result.roles && result.roles[0]) || role;

        if (!token) throw new Error("Login response içinde token bulunamadı.");

        localStorage.setItem("token", token);
        localStorage.setItem("role", serverRole);

        window.location.href = serverRole === "ADMIN" ? "/admin/dashboard" : "/doctor/dashboard";
    } catch (err) {
        errorEl.textContent = err.message || "Login başarısız.";
    }
});
