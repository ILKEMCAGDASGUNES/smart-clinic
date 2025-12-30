import { renderHeader } from "./components/header.js";
import { openModal, closeModal } from "./components/modal.js";
import { doctorCard } from "./components/doctorCard.js";
import {
    getAllDoctors,
    deleteDoctor,
    addDoctor,
    searchDoctors,
    filterDoctors,
} from "./services/doctorService.js";

function uniqueSorted(values) {
    return [...new Set(values.filter(Boolean))].sort((a, b) => a.localeCompare(b));
}

function fillSelect(selectEl, items, defaultLabel) {
    if (!selectEl) return;
    selectEl.innerHTML = `
    <option value="">${defaultLabel}</option>
    ${items.map(x => `<option value="${x}">${x}</option>`).join("")}
  `;
}



function ensureAuth() {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");
    if (!token) window.location.href = "/";
    if (role && role !== "ADMIN") window.location.href = "/doctor/dashboard";
}

ensureAuth();
renderHeader("ADMIN");

const doctorListEl = document.getElementById("doctorList");
doctorSearchEl?.addEventListener("input", applyFilters);
specialtyFilterEl?.addEventListener("change", applyFilters);
timeFilterEl?.addEventListener("change", applyFilters);
const openAddDoctorModalBtn = document.getElementById("openAddDoctorModal");

let currentDoctors = [];

function renderDoctors(doctors) {
    currentDoctors = doctors || [];
    if (!doctorListEl) return;

    doctorListEl.innerHTML = currentDoctors
        .map((d) => doctorCard(d, { onDelete: true }))
        .join("");

    // delete button events
    doctorListEl.querySelectorAll(".deleteDoctorBtn").forEach((btn) => {
        btn.addEventListener("click", async () => {
            const id = btn.getAttribute("data-id");
            if (!id) return;

            if (!confirm("Delete this doctor?")) return;

            try {
                await deleteDoctor(id);
                await loadDoctors();
            } catch (e) {
                alert(e.message);
            }
        });
    });
}

async function loadDoctors() {
    const doctors = await getAllDoctors();
    const list = Array.isArray(doctors) ? doctors : (doctors.items || doctors.data || []);

    // dropdown verisi üret
    const specialties = uniqueSorted(list.map(d => d.specialty ?? d.department));
    const times = uniqueSorted(list.map(d => d.availableTime ?? d.shift));

    fillSelect(specialtyFilterEl, specialties, "All Specialties");
    fillSelect(timeFilterEl, times, "All Times");

    renderDoctors(list);
}


function openAddDoctor() {
    openModal(
        "Add Doctor",
        `
    <form id="addDoctorForm">
      <label>Name</label>
      <input id="docName" style="width:100%;padding:8px;margin:6px 0;" placeholder="Dr. John Doe"/>

      <label>Email</label>
      <input id="docEmail" type="email" style="width:100%;padding:8px;margin:6px 0;" placeholder="john@clinic.com"/>

      <label>Specialty</label>
      <input id="docSpecialty" style="width:100%;padding:8px;margin:6px 0;" placeholder="Cardiology"/>

      <label>Available Time</label>
      <input id="docTime" style="width:100%;padding:8px;margin:6px 0;" placeholder="09:00-17:00"/>

      <button type="submit" style="margin-top:10px;">Create</button>
    </form>
    `
    );

    document.getElementById("addDoctorForm")?.addEventListener("submit", async (e) => {
        e.preventDefault();

        const payload = {
            name: document.getElementById("docName").value.trim(),
            email: document.getElementById("docEmail").value.trim(),
            specialty: document.getElementById("docSpecialty").value.trim(),
            availableTime: document.getElementById("docTime").value.trim(),
        };

        try {
            await addDoctor(payload);
            closeModal();
            await loadDoctors();
        } catch (err) {
            alert(err.message);
        }
    });
}

// Search
doctorSearchEl?.addEventListener("input", async () => {
    try {
        const name = doctorSearchEl.value.trim();
        const doctors = await searchDoctors(name);
        renderDoctors(Array.isArray(doctors) ? doctors : (doctors.items || doctors.data || []));
    } catch (e) {
        console.warn(e);
    }
});

// Filter
async function applyFilters() {
    try {
        const searchText = doctorSearchEl?.value?.trim() || "";
        const specialty = specialtyFilterEl?.value || "";
        const time = timeFilterEl?.value || "";

        // Search varsa search baskın
        if (searchText) {
            const doctors = await searchDoctors(searchText);
            renderDoctors(Array.isArray(doctors) ? doctors : (doctors.items || doctors.data || []));
            return;
        }

        // Filtrelerden en az biri seçiliyse filter kullan
        if (specialty || time) {
            const doctors = await filterDoctors({ specialty, time });
            renderDoctors(Array.isArray(doctors) ? doctors : (doctors.items || doctors.data || []));
            return;
        }

        // hiçbiri yoksa tüm liste
        await loadDoctors();
    } catch (e) {
        console.warn(e);
    }
}


specialtyFilterEl?.addEventListener("change", applyFilters);
timeFilterEl?.addEventListener("change", applyFilters);

// Add doctor modal
openAddDoctorModalBtn?.addEventListener("click", openAddDoctor);

// initial load
loadDoctors().catch((e) => {
    console.error(e);
    doctorListEl.innerHTML = `<p style="color:#b00020;">Doctors could not be loaded: ${e.message}</p>`;
});
