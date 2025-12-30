import { request } from "../utils/httpClient.js";

/**
 * ✅ Endpoint’leri tek yerden yönet
 * Sende farklıysa sadece burayı değiştir.
 */
const API = {
    myAppointments: "/api/appointments/my", // örn: token’dan doctor çıkar
    appointmentsByDate: (date) => `/api/appointments/my?date=${encodeURIComponent(date)}`,
    searchAppointments: (name) => `/api/appointments/my/search?patient=${encodeURIComponent(name)}`,
    prescriptionsByPatient: (patientId) => `/api/patients/${patientId}/prescriptions`,
};

export async function getMyAppointments() {
    return request(API.myAppointments);
}

export async function searchMyAppointmentsByPatientName(name) {
    if (!name) return getMyAppointments();
    return request(API.searchAppointments(name));
}

export async function filterMyAppointmentsByDate(date) {
    if (!date) return getMyAppointments();
    return request(API.appointmentsByDate(date));
}

export async function getPatientPrescriptions(patientId) {
    return request(API.prescriptionsByPatient(patientId));
}
