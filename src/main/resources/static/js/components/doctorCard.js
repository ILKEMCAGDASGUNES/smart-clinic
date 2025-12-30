export function doctorCard(doctor, { onDelete } = {}) {
    const id = doctor.id ?? doctor.doctorId ?? "";
    const name = doctor.name ?? `${doctor.firstName ?? ""} ${doctor.lastName ?? ""}`.trim();
    const specialty = doctor.specialty ?? doctor.department ?? "-";
    const time = doctor.availableTime ?? doctor.shift ?? "-";
    const email = doctor.email ?? "-";

    return `
    <div class="card" data-id="${id}" style="border:1px solid #ddd;border-radius:10px;padding:12px;margin:10px 0;">
      <div style="display:flex;justify-content:space-between;gap:12px;">
        <div>
          <div style="font-weight:700;">${name || "Doctor"}</div>
          <div style="margin-top:6px;font-size:14px;">
            <div><b>Specialty:</b> ${specialty}</div>
            <div><b>Time:</b> ${time}</div>
            <div><b>Email:</b> ${email}</div>
          </div>
        </div>

        ${onDelete ? `<button class="deleteDoctorBtn" data-id="${id}">Delete</button>` : ""}
      </div>
    </div>
  `;
}
