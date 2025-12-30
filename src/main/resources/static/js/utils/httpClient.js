export async function request(url, options = {}) {
    const token = localStorage.getItem("token");

    const headers = {
        "Content-Type": "application/json",
        ...(options.headers || {}),
    };

    if (token) headers["Authorization"] = `Bearer ${token}`;

    const res = await fetch(url, { ...options, headers });

    if (!res.ok) {
        let message = `HTTP ${res.status}`;
        try {
            const data = await res.json();
            message = data.message || data.error || JSON.stringify(data);
        } catch (_) {
            // ignore
        }
        throw new Error(message);
    }

    // 204 No Content ise json parse etmeyelim
    if (res.status === 204) return null;
    return res.json();
}
