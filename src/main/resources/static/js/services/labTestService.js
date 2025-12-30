import { request } from "../utils/httpClient.js";

/**
 * Sende endpoint farklıysa sadece burayı değiştir.
 * Örnekler:
 * - GET /api/labtests/appointment/{id}
 * - GET /api/lab-tests?appointmentId=...
 */
const API = {
    byAppointment: (appointmentId) => `/api/labtests/appointment/${appointmentId}`,
    // alternatif: byAppointment: (appointmentId) => `/api/lab-tests?appointmentId=${encodeURIComponent(appointmentId)}`,
};

export async function getLabTestsByAppointment(appointmentId) {
    return request(API.byAppointment(appointmentId));
}
