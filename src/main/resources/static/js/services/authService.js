import { request } from "../utils/httpClient.js";

export async function login(email, password) {
    // ⚠️ Buradaki endpoint’i senin backend’ine göre ayarlayacağız.
    // Çok yaygın olan örnek:
    // POST /api/auth/login  body: { email, password }

    return request("/api/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, password }),
    });
}
