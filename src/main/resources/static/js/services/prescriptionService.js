import { request } from "../utils/httpClient.js";

/**
 * Sende endpoint farklıysa sadece burayı değiştir.
 * Örnekler:
 * - GET  /api/patients/{id}/prescriptions
 * - POST /api/prescriptions
 */
const API = {
    byPatient: (patientId) => `/api/patients/${patientId}/prescriptions`,
    create: "/api/prescriptions",
};

export async function getPrescriptionsByPatient(patientId) {
    return request(API.byPatient(patientId));
}

export async function createPrescription(payload) {
    return request(API.create, {
        method: "POST",
        body: JSON.stringify(payload),
    });
}
