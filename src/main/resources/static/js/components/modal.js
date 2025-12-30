export function openModal(title, bodyHtml) {
    const root = document.getElementById("modalRoot");
    if (!root) return;

    root.innerHTML = `
    <div id="modalOverlay" style="position:fixed;inset:0;background:rgba(0,0,0,.35);display:flex;align-items:center;justify-content:center;padding:20px;">
      <div style="background:#fff;border-radius:12px;max-width:520px;width:100%;padding:16px;">
        <div style="display:flex;justify-content:space-between;align-items:center;gap:12px;">
          <h3 style="margin:0;">${title}</h3>
          <button id="closeModalBtn" type="button">X</button>
        </div>
        <div style="margin-top:12px;">${bodyHtml}</div>
      </div>
    </div>
  `;

    document.getElementById("closeModalBtn")?.addEventListener("click", closeModal);
    document.getElementById("modalOverlay")?.addEventListener("click", (e) => {
        if (e.target?.id === "modalOverlay") closeModal();
    });
}

export function closeModal() {
    const root = document.getElementById("modalRoot");
    if (root) root.innerHTML = "";
}
