import { request } from "../utils/httpClient.js";

/**
 * ✅ Endpoint’leri tek yerden yönet
 * Sende farklıysa sadece burayı değiştir.
 */
const API = {
    list: "/api/doctors",
    create: "/api/doctors",
    delete: (id) => `/api/doctors/${id}`,
    search: (name) => `/api/doctors/search?name=${encodeURIComponent(name)}`,
    filter: ({ specialty, time }) =>
        `/api/doctors/filter?specialty=${encodeURIComponent(specialty || "")}&time=${encodeURIComponent(time || "")}`,
};

export async function getAllDoctors() {
    return request(API.list);
}

export async function addDoctor(payload) {
    return request(API.create, {
        method: "POST",
        body: JSON.stringify(payload),
    });
}

export async function deleteDoctor(id) {
    return request(API.delete(id), { method: "DELETE" });
}

export async function searchDoctors(name) {
    if (!name) return getAllDoctors();
    return request(API.search(name));
}

export async function filterDoctors({ specialty, time }) {
    return request(API.filter({ specialty, time }));
}
