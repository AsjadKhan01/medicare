// =========================================================
// MediCare — Find a Doctor (Patient view) — interactions
// =========================================================

document.addEventListener('DOMContentLoaded', () => {
  initExpandCollapse();
  initSlotSelection();
  initConfirmBooking();
  initSearchAndFilters();
  initClearFilters();
});

/* ---------- Expand / collapse doctor card ---------- */
function initExpandCollapse() {
  const cards = document.querySelectorAll('.doctor-card');

  cards.forEach((card) => {
    const row = card.querySelector('[data-toggle="expand"]');
    const picker = card.querySelector('.slot-picker');
    if (!row || !picker) return;

    row.addEventListener('click', () => {
      const isExpanded = card.classList.contains('is-expanded');

      // Collapse any other open card (accordion-style, keeps the page tidy)
      cards.forEach((other) => {
        if (other !== card && other.classList.contains('is-expanded')) {
          collapseCard(other);
        }
      });

      if (isExpanded) {
        collapseCard(card);
      } else {
        expandCard(card, picker);
      }
    });
  });
}

function expandCard(card, picker) {
  card.classList.add('is-expanded');
  picker.style.maxHeight = picker.scrollHeight + 'px';
}

function collapseCard(card) {
  const picker = card.querySelector('.slot-picker');
  card.classList.remove('is-expanded');
  if (picker) picker.style.maxHeight = '0px';
}

/* ---------- Slot selection ---------- */
function initSlotSelection() {
  document.querySelectorAll('.slot-options').forEach((group) => {
    const doctorId = group.dataset.doctor;

    group.addEventListener('click', (e) => {
      const btn = e.target.closest('.slot-btn');
      if (!btn || btn.classList.contains('slot-btn--taken') || btn.disabled) return;

      // Clear selection across ALL slot groups for this doctor (multiple day groups)
      document.querySelectorAll(`.slot-options[data-doctor="${doctorId}"] .slot-btn`)
        .forEach((b) => b.classList.remove('is-selected'));

      btn.classList.add('is-selected');

      const note = document.getElementById(`selectedNote-${doctorId}`);
      const confirmBtn = document.getElementById(`confirmBtn-${doctorId}`);
      if (note) {
        note.textContent = `Selected: ${btn.dataset.time}`;
        note.classList.add('has-selection');
      }
      if (confirmBtn) confirmBtn.disabled = false;
    });
  });
}

/* ---------- Confirm booking ---------- */
function initConfirmBooking() {
  document.querySelectorAll('.btn-confirm-booking').forEach((btn) => {
    btn.addEventListener('click', () => {
      const doctorId = btn.id.replace('confirmBtn-', '');
      const card = document.querySelector(`[data-doctor-id="${doctorId}"]`);
      const doctorName = card?.dataset.name || 'the doctor';
      const selectedSlot = document.querySelector(`.slot-options[data-doctor="${doctorId}"] .slot-btn.is-selected`);
      const reasonInput = document.getElementById(`reason-${doctorId}`);
      const reason = reasonInput?.value.trim();

      if (!selectedSlot) return;

      const timeLabel = selectedSlot.dataset.time;

      btn.disabled = true;
      const originalHTML = btn.innerHTML;
      btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Sending request...';

      setTimeout(() => {
        showToast(`Request sent to ${doctorName} for ${timeLabel}${reason ? ' · ' + reason : ''}. Waiting for confirmation.`, 'success');

        // Reset this picker's state
        btn.innerHTML = originalHTML;
        selectedSlot.classList.remove('is-selected');
        const note = document.getElementById(`selectedNote-${doctorId}`);
        if (note) {
          note.textContent = 'Pick a slot to continue';
          note.classList.remove('has-selection');
        }
        if (reasonInput) reasonInput.value = '';
        btn.disabled = true;

        // Collapse the card after booking
        if (card) collapseCard(card);
      }, 700);
    });
  });
}

/* ---------- Search + filters ---------- */
function initSearchAndFilters() {
  const searchInput = document.getElementById('searchInput');
  const specFilter = document.getElementById('specializationFilter');
  const availFilter = document.getElementById('availabilityFilter');

  [searchInput, specFilter, availFilter].forEach((el) => {
    if (!el) return;
    const eventName = el.tagName === 'SELECT' ? 'change' : 'input';
    el.addEventListener(eventName, applyFilters);
  });
}

function applyFilters() {
  const searchTerm = (document.getElementById('searchInput')?.value || '').toLowerCase().trim();
  const spec = document.getElementById('specializationFilter')?.value || '';
  const avail = document.getElementById('availabilityFilter')?.value || '';

  const cards = document.querySelectorAll('.doctor-card');
  let visibleCount = 0;

  cards.forEach((card) => {
    const name = (card.dataset.name || '').toLowerCase();
    const cardSpec = card.dataset.spec || '';
    const cardAvail = card.dataset.availability || '';

    const matchesSearch = !searchTerm || name.includes(searchTerm);
    const matchesSpec = !spec || cardSpec === spec;
    const matchesAvail = !avail || cardAvail === avail;

    const isVisible = matchesSearch && matchesSpec && matchesAvail;
    card.classList.toggle('is-hidden', !isVisible);
    if (isVisible) visibleCount++;
  });

  const emptyState = document.getElementById('noResultsState');
  if (emptyState) emptyState.classList.toggle('d-none', visibleCount !== 0);
}

/* ---------- Clear filters ---------- */
function initClearFilters() {
  const clearBtn = document.getElementById('clearFiltersBtn');
  if (!clearBtn) return;

  clearBtn.addEventListener('click', () => {
    const searchInput = document.getElementById('searchInput');
    const specFilter = document.getElementById('specializationFilter');
    const availFilter = document.getElementById('availabilityFilter');

    if (searchInput) searchInput.value = '';
    if (specFilter) specFilter.value = '';
    if (availFilter) availFilter.value = '';

    applyFilters();
  });
}

/* ---------- Toasts ---------- */
function showToast(message, type = 'success') {
  const stack = document.getElementById('toastStack');
  if (!stack) return;

  const icon = type === 'success' ? 'bi-check-circle-fill' : 'bi-info-circle-fill';

  const toast = document.createElement('div');
  toast.className = `app-toast toast--${type}`;
  toast.innerHTML = `<i class="bi ${icon}"></i><span>${message}</span>`;
  stack.appendChild(toast);

  setTimeout(() => {
    toast.classList.add('toast--out');
    setTimeout(() => toast.remove(), 200);
  }, 3200);
}