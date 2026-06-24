// =========================================================
// MediCare Doctor Dashboard — interactions
// =========================================================

document.addEventListener('DOMContentLoaded', () => {
  setTodayDate();
  animateStatCounters();
  initRequestActions();
  initCompleteButtons();
  initSaveSchedule();
  initAddSlotButtons();
});

/* ---------- Today's date ---------- */
function setTodayDate() {
  const el = document.getElementById('todayDate');
  if (!el) return;
  const today = new Date();
  const formatted = today.toLocaleDateString('en-GB', {
    weekday: 'long',
    day: 'numeric',
    month: 'long'
  });
  el.textContent = formatted;
}

/* ---------- Animated stat counters ---------- */
function animateStatCounters() {
  const figures = document.querySelectorAll('.stat-figure');
  figures.forEach((el) => {
    const target = parseInt(el.dataset.count, 10) || 0;
    const duration = 700;
    const start = performance.now();

    function tick(now) {
      const progress = Math.min((now - start) / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3); // ease-out cubic
      el.textContent = Math.round(eased * target);
      if (progress < 1) {
        requestAnimationFrame(tick);
      } else {
        el.textContent = target;
      }
    }
    requestAnimationFrame(tick);
  });
}

/* ---------- Pending request: Accept / Reject ---------- */
function initRequestActions() {
  const list = document.getElementById('requestList');
  const emptyState = document.getElementById('requestEmptyState');
  const countBadge = document.getElementById('pendingCount');

  if (!list) return;

  list.addEventListener('click', (e) => {
    const acceptBtn = e.target.closest('.btn-accept');
    const rejectBtn = e.target.closest('.btn-reject');
    if (!acceptBtn && !rejectBtn) return;

    const card = e.target.closest('.request-card');
    if (!card) return;

    const name = card.dataset.name || 'Patient';

    if (acceptBtn) {
      showToast(`${name}'s appointment confirmed`, 'success');
    } else if (rejectBtn) {
      showToast(`${name}'s request rejected`, 'danger');
    }

    removeRequestCard(card, list, emptyState, countBadge);
  });
}

function removeRequestCard(card, list, emptyState, countBadge) {
  card.classList.add('is-leaving');
  setTimeout(() => {
    card.remove();
    const remaining = list.querySelectorAll('.request-card').length;
    if (countBadge) countBadge.textContent = remaining;
    if (remaining === 0 && emptyState) {
      emptyState.classList.remove('d-none');
    }
  }, 220);
}

/* ---------- Confirmed queue: Mark Complete ---------- */
function initCompleteButtons() {
  const body = document.getElementById('queueBody');
  if (!body) return;

  body.addEventListener('click', (e) => {
    const btn = e.target.closest('.btn-complete');
    if (!btn) return;

    const row = btn.closest('tr');
    const patientName = row.querySelector('.queue-patient')?.textContent.trim() || 'Patient';
    const statusPill = row.querySelector('.status-pill');

    row.classList.add('is-complete');
    if (statusPill) {
      statusPill.textContent = 'Done';
      statusPill.classList.remove('status-pill--confirmed');
      statusPill.classList.add('status-pill--done');
    }
    btn.innerHTML = '<i class="bi bi-check2-circle"></i> Completed';
    btn.disabled = true;

    showToast(`Marked ${patientName} as complete`, 'success');
  });
}

/* ---------- Save Schedule ---------- */
function initSaveSchedule() {
  const btn = document.getElementById('saveScheduleBtn');
  const durationSelect = document.getElementById('slotDuration');
  if (!btn) return;

  btn.addEventListener('click', () => {
    const duration = durationSelect ? durationSelect.value : '30';
    btn.disabled = true;
    const originalHTML = btn.innerHTML;
    btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Saving...';

    setTimeout(() => {
      btn.innerHTML = originalHTML;
      btn.disabled = false;
      showToast(`Schedule saved · ${duration} min slots`, 'success');
    }, 600);
  });
}

/* ---------- Add slot (demo behaviour) ---------- */
function initAddSlotButtons() {
  const list = document.getElementById('availList');
  if (!list) return;

  list.addEventListener('click', (e) => {
    const addBtn = e.target.closest('.btn-add-slot');
    if (!addBtn) return;

    const row = addBtn.closest('.avail-row');
    const day = row?.dataset.day || 'this day';

    const chip = document.createElement('span');
    chip.className = 'slot-chip';
    chip.textContent = 'New slot';
    addBtn.insertAdjacentElement('beforebegin', chip);

    showToast(`Slot added for ${day}`, 'success');
  });
}

/* ---------- Toasts ---------- */
function showToast(message, type = 'success') {
  const stack = document.getElementById('toastStack');
  if (!stack) return;

  const icon = type === 'success' ? 'bi-check-circle-fill'
             : type === 'danger' ? 'bi-x-circle-fill'
             : 'bi-info-circle-fill';

  const toast = document.createElement('div');
  toast.className = `app-toast toast--${type}`;
  toast.innerHTML = `<i class="bi ${icon}"></i><span>${message}</span>`;
  stack.appendChild(toast);

  setTimeout(() => {
    toast.classList.add('toast--out');
    setTimeout(() => toast.remove(), 200);
  }, 2600);
}