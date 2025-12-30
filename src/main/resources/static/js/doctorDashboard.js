import { renderHeader } from "./components/header.js";
import { openModal, closeModal } from "./components/modal.js";
import {
    getMyAppointments,
    searchMyAppointmentsByPatientName,
    filterMyAppointmentsByDate,
} from "./services/patientService.js";

import { getLabTestsByAppointment } from "./services/labTestService.js";
import { getPrescriptionsByPatient, createPrescription } from "./services/prescriptionService.js";

function ensureAuth() {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");
    if (!token) window.location.href = "/";
    if (role && role !== "DOCTOR") window.location.href = "/admin/dashboard";
}

ensureAuth();
renderHeader("DOCTOR");

const appointmentListEl = document.getElementById("appointmentList");
const appointmentSearchEl = document.getElementById("appointmentSearch");
const dateFilterEl = document.getElementById("dateFilter");

function appointmentRow(a) {
    const id = a.id ?? a.appointmentId ?? "";
    const patientId = a.patientId ?? a.patient?.id ?? "";
    const patientName = a.patientName ?? `${a.patientFirstName ?? ""} ${a.patientLastName ?? ""}`.trim();
    const dateTime = a.appointmentTime ?? a.dateTime ?? a.time ?? "-";
    const status = a.status ?? (a.canceled ? "CANCELED" : "SCHEDULED");

    return `
    <div style="border:1px solid #ddd;border-radius:12px;padding:12px;margin:10px 0;">
      <div style="display:flex;justify-content:space-between;gap:12px;align-items:flex-start;">
        <div>
          <div style="font-weight:700;">${patientName || "Patient"}</div>
          <div style="margin-top:6px;font-size:14px;line-height:1.5;">
            <div><b>Appointment:</b> ${dateTime}</div>
            <div><b>Status:</b> ${status}</div>
            <div><b>Appointment id:</b> ${id}</div>
          </div>
        </div>

        <div style="display:flex;gap:8px;flex-wrap:wrap;">
          <button class="viewPresBtn" data-patient-id="${patientId}">Prescriptions</button>
          <button class="openLabBtn" data-appointment-id="${id}">Lab Results</button>
          <button class="prescribeBtn" data-patient-id="${patientId}" data-appointment-id="${id}">Prescribe</button>
        </div>
      </div>
    </div>
  `;
}



function renderAppointments(list) {
    const arr = Array.isArray(list) ? list : (list.items || list.data || []);
    appointmentListEl.innerHTML = arr.map(appointmentRow).join("");

    /* =======================
       VIEW PRESCRIPTIONS
    ======================= */
    appointmentListEl.querySelectorAll(".viewPresBtn").forEach((btn) => {
        btn.addEventListener("click", async () => {
            const patientId = btn.getAttribute("data-patient-id");
            if (!patientId) {
                alert("patientId not found in appointment payload.");
                return;
            }

            try {
                const prescriptions = await getPrescriptionsByPatient(patientId);
                const items = Array.isArray(prescriptions)
                    ? prescriptions
                    : (prescriptions.items || prescriptions.data || []);

                const html = items.length
                    ? `
            <ul>
              ${items.map(p => `
                <li style="margin:6px 0;">
                  <b>${p.medication ?? p.drugName ?? "Medication"}</b>
                  ${p.dosage ? `— Dosage: ${p.dosage}` : ""}
                  ${p.instructions ? `— Notes: ${p.instructions}` : ""}
                </li>
              `).join("")}
            </ul>
          `
                    : `<p>No prescriptions found.</p>`;

                openModal("Patient Prescriptions", html);
            } catch (e) {
                openModal("Patient Prescriptions", `<p style="color:#b00020;">${e.message}</p>`);
            }
        });
    });

    /* =======================
       LAB RESULTS
    ======================= */
    appointmentListEl.querySelectorAll(".openLabBtn").forEach((btn) => {
        btn.addEventListener("click", async () => {
            const appointmentId = btn.getAttribute("data-appointment-id");
            if (!appointmentId) return;

            try {
                const tests = await getLabTestsByAppointment(appointmentId);
                const items = Array.isArray(tests)
                    ? tests
                    : (tests.items || tests.data || []);

                const html = items.length
                    ? `
            <ul>
              ${items.map(t => `
                <li style="margin:6px 0;">
                  <b>${t.testName ?? t.name ?? "Test"}</b>
                  — Result: ${t.result ?? t.value ?? "-"}
                  — Status: ${t.status ?? "-"}
                </li>
              `).join("")}
            </ul>
          `
                    : `<p>No lab results found.</p>`;

                openModal("Lab Results", html);
            } catch (e) {
                openModal("Lab Results", `<p style="color:#b00020;">${e.message}</p>`);
            }
        });
    });

    /* =======================
       PRESCRIBE (CREATE)
    ======================= */
    appointmentListEl.querySelectorAll(".prescribeBtn").forEach((btn) => {
        btn.addEventListener("click", () => {
            const patientId = btn.getAttribute("data-patient-id");
            const appointmentId = btn.getAttribute("data-appointment-id");
            if (!patientId || !appointmentId) return;

            openModal("Create Prescription", `
        <form id="prescriptionForm">
          <label>Medication</label>
          <input id="medication" required style="width:100%;padding:8px;margin:6px 0;" />

          <label>Dosage</label>
          <input id="dosage" style="width:100%;padding:8px;margin:6px 0;" />

          <label>Instructions</label>
          <textarea id="instructions" rows="3" style="width:100%;padding:8px;margin:6px 0;"></textarea>

          <button type="submit" style="margin-top:10px;">Save</button>
        </form>
      `);

            document.getElementById("prescriptionForm")
                ?.addEventListener("submit", async (e) => {
                    e.preventDefault();

                    const payload = {
                        patientId: Number(patientId),
                        appointmentId: Number(appointmentId),
                        medication: document.getElementById("medication").value.trim(),
                        dosage: document.getElementById("dosage").value.trim(),
                        instructions: document.getElementById("instructions").value.trim(),
                    };

                    try {
                        await createPrescription(payload);
                        closeModal();
                        alert("Prescription created successfully ✅");
                    } catch (err) {
                        alert(err.message);
                    }
                });
        });
    });
}


async function loadAppointments() {
    const list = await getMyAppointments();
    renderAppointments(list);
}

appointmentSearchEl?.addEventListener("input", async () => {
    try {
        const name = appointmentSearchEl.value.trim();
        const list = await searchMyAppointmentsByPatientName(name);
        renderAppointments(list);
    } catch (e) {
        console.warn(e);
    }
});

dateFilterEl?.addEventListener("change", async () => {
    try {
        const date = dateFilterEl.value; // yyyy-mm-dd
        const list = await filterMyAppointmentsByDate(date);
        renderAppointments(list);
    } catch (e) {
        console.warn(e);
    }
});

loadAppointments().catch((e) => {
    console.error(e);
    appointmentListEl.innerHTML = `<p style="color:#b00020;">Appointments could not be loaded: ${e.message}</p>`;
});
