export function renderHeader(role) {
    const container = document.getElementById("header");
    if (!container) return;

    container.innerHTML = `
    <div style="display:flex;justify-content:space-between;align-items:center;gap:12px;padding:12px 0;">
      <div><strong>SmartClinic</strong> — ${role} Panel</div>
      <div style="display:flex;gap:10px;">
        <a href="${role === "ADMIN" ? "/admin/dashboard" : "/doctor/dashboard"}">Dashboard</a>
        <button id="logoutBtn" type="button">Logout</button>
      </div>
    </div>
    <hr/>
  `;

    const btn = document.getElementById("logoutBtn");
    btn?.addEventListener("click", () => {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        window.location.href = "/";
    });
}
