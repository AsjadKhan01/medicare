// =========================================================
// MediCare — Admin Panel — interactions
// =========================================================

document.addEventListener('DOMContentLoaded', () => {
  setTodayDate();
  initSidebarNav();
  initSidebarToggle();
  animateStatCounters();
  renderBarChart();
  initHireModal();
  initDoctorTableFilters();
  initRowActions();
});

/* ---------- Today's date ---------- */
function setTodayDate() {
  const el = document.getElementById('todayDate');
  if (!el) return;
  const today = new Date();
  el.textContent = today.toLocaleDateString('en-GB', {
    weekday: 'long',
    day: 'numeric',
    month: 'long'
  });
}

/* ---------- Sidebar nav: switch views ---------- */
function initSidebarNav() {
  const links = document.querySelectorAll('.nav-link[data-view]');
  const panels = document.querySelectorAll('.view-panel');

  links.forEach((link) => {
    link.addEventListener('click', () => {
      const target = link.dataset.view;

      links.forEach((l) => l.classList.remove('is-active'));
      link.classList.add('is-active');

      panels.forEach((panel) => {
        panel.classList.toggle('is-active', panel.id === `view-${target}`);
      });

      // Close sidebar on mobile after navigating
      const sidebar = document.getElementById('sidebar');
      if (sidebar && window.innerWidth < 992) {
        sidebar.classList.remove('is-open');
      }

      // Re-render chart if navigating back to overview (CSS vars need a fresh paint)
      if (target === 'overview') {
        renderBarChart();
      }
    });
  });
}

/* ---------- Mobile sidebar toggle ---------- */
function initSidebarToggle() {
  const toggle = document.getElementById('sidebarToggle');
  const sidebar = document.getElementById('sidebar');
  if (!toggle || !sidebar) return;

  toggle.addEventListener('click', () => {
    sidebar.classList.toggle('is-open');
  });

  document.addEventListener('click', (e) => {
    if (window.innerWidth < 992 &&
        sidebar.classList.contains('is-open') &&
        !sidebar.contains(e.target) &&
        !toggle.contains(e.target)) {
      sidebar.classList.remove('is-open');
    }
  });
}

/* ---------- Animated stat counters ---------- */
function animateStatCounters() {
  document.querySelectorAll('.stat-figure').forEach((el) => {
    const target = parseInt(el.dataset.count, 10) || 0;
    const duration = 700;
    const start = performance.now();

    function tick(now) {
      const progress = Math.min((now - start) / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      el.textContent = Math.round(eased * target).toLocaleString();
      if (progress < 1) {
        requestAnimationFrame(tick);
      } else {
        el.textContent = target.toLocaleString();
      }
    }
    requestAnimationFrame(tick);
  });
}

/* ---------- Bar chart (pure CSS via custom properties) ---------- */
function renderBarChart() {
  const chart = document.getElementById('weeklyChart');
  if (!chart) return;

  const cols = chart.querySelectorAll('.bar-col');
  let maxTotal = 0;
  cols.forEach((col) => {
    const total = (parseInt(col.dataset.confirmed, 10) || 0) + (parseInt(col.dataset.pending, 10) || 0);
    if (total > maxTotal) maxTotal = total;
  });
  if (maxTotal === 0) maxTotal = 1;

  cols.forEach((col) => {
    const confirmed = parseInt(col.dataset.confirmed, 10) || 0;
    const pending = parseInt(col.dataset.pending, 10) || 0;

    const confirmedPct = (confirmed / maxTotal) * 100;
    const pendingPct = (pending / maxTotal) * 100;

    col.style.setProperty('--confirmed-h', confirmedPct.toFixed(1));
    col.style.setProperty('--pending-h', pendingPct.toFixed(1));

    // Add day label below the bar if not already present
    if (!col.querySelector('.bar-col-label')) {
      const label = document.createElement('span');
      label.className = 'bar-col-label';
      label.textContent = col.dataset.day || '';
      col.appendChild(label);
    }
  });
}

/* ---------- Hire Doctor modal ---------- */
function initHireModal() {
  const overlay = document.getElementById('hireModalOverlay');
  const openBtn = document.getElementById('openHireModalBtn');
  const closeBtn = document.getElementById('closeHireModalBtn');
  const cancelBtn = document.getElementById('cancelHireBtn');
  const form = document.getElementById('hireDoctorForm');

  if (!overlay || !form) return;

  const open = () => overlay.classList.add('is-open');
  const close = () => {
    overlay.classList.remove('is-open');
    form.reset();
  };

  openBtn?.addEventListener('click', open);
  closeBtn?.addEventListener('click', close);
  cancelBtn?.addEventListener('click', close);

  overlay.addEventListener('click', (e) => {
    if (e.target === overlay) close();
  });

  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && overlay.classList.contains('is-open')) close();
  });

  form.addEventListener('submit', (e) => {
    e.preventDefault();

    const name = document.getElementById('hireName').value.trim();
    const dept = document.getElementById('hireDept').value;
    const exp = document.getElementById('hireExp').value;
    const hospital = document.getElementById('hireHospital').value.trim() || 'MediCare Network';

    if (!name || !dept || !exp) return;

    addDoctorToTable(name, dept, exp, hospital);
    close();
    showToast(`${name} was added to ${dept}`, 'success');

    // Bump the "Active doctors" stat
    bumpDoctorCount();
  });
}

/* ---------- Add a new doctor row to the table ---------- */
function addDoctorToTable(name, dept, exp, hospital) {
  const tbody = document.getElementById('doctorTableBody');
  if (!tbody) return;

  const initials = name
    .replace(/^Dr\.?\s*/i, '')
    .split(' ')
    .filter(Boolean)
    .map((w) => w[0])
    .join('')
    .slice(0, 2)
    .toUpperCase();

  const row = document.createElement('tr');
  row.dataset.name = name;
  row.dataset.dept = dept;
  row.dataset.status = 'active';

  row.innerHTML = `
    <td>
      <div class="person-cell">
        <span class="avatar-sm avatar-sm--teal">${initials}</span>
        <div>
          <div class="person-name">${escapeHtml(name)}</div>
          <div class="person-sub">${escapeHtml(hospital)}</div>
        </div>
      </div>
    </td>
    <td>${escapeHtml(dept)}</td>
    <td>${escapeHtml(exp)} yrs</td>
    <td>0</td>
    <td><span class="status-pill status-pill--active">Active</span></td>
    <td class="text-end">
      <button class="btn-icon-action" data-action="edit" title="Edit"><i class="bi bi-pencil"></i></button>
      <button class="btn-icon-action btn-icon-action--danger" data-action="remove" title="Remove"><i class="bi bi-trash"></i></button>
    </td>
  `;

  tbody.prepend(row);
}

function escapeHtml(str) {
  const div = document.createElement('div');
  div.textContent = str;
  return div.innerHTML;
}

/* ---------- Bump "Active doctors" stat after hiring ---------- */
function bumpDoctorCount() {
  const statCards = document.querySelectorAll('.stat-card--teal .stat-figure');
  statCards.forEach((el) => {
    const current = parseInt(el.textContent.replace(/,/g, ''), 10) || 0;
    el.textContent = (current + 1).toLocaleString();
  });

  const navBadge = document.querySelector('.nav-link[data-view="doctors"] .nav-badge');
  if (navBadge) {
    const current = parseInt(navBadge.textContent, 10) || 0;
    navBadge.textContent = current + 1;
  }
}

/* ---------- Doctor table: search + filters ---------- */
function initDoctorTableFilters() {
  const searchInput = document.getElementById('doctorSearchInput');
  const deptFilter = document.getElementById('doctorDeptFilter');
  const statusFilter = document.getElementById('doctorStatusFilter');
  const tbody = document.getElementById('doctorTableBody');
  const emptyState = document.getElementById('doctorEmptyState');

  if (!tbody) return;

  function applyFilters() {
    const term = (searchInput?.value || '').toLowerCase().trim();
    const dept = deptFilter?.value || '';
    const status = statusFilter?.value || '';

    let visibleCount = 0;
    tbody.querySelectorAll('tr').forEach((row) => {
      const name = (row.dataset.name || '').toLowerCase();
      const rowDept = row.dataset.dept || '';
      const rowStatus = row.dataset.status || '';

      const matches = (!term || name.includes(term)) &&
                       (!dept || rowDept === dept) &&
                       (!status || rowStatus === status);

      row.classList.toggle('is-hidden', !matches);
      if (matches) visibleCount++;
    });

    if (emptyState) emptyState.classList.toggle('d-none', visibleCount !== 0);
  }

  searchInput?.addEventListener('input', applyFilters);
  deptFilter?.addEventListener('change', applyFilters);
  statusFilter?.addEventListener('change', applyFilters);
}

/* ---------- Row actions: edit / remove ---------- */
function initRowActions() {
  const tbody = document.getElementById('doctorTableBody');
  if (!tbody) return;

  tbody.addEventListener('click', (e) => {
    const btn = e.target.closest('.btn-icon-action');
    if (!btn) return;

    const row = btn.closest('tr');
    const name = row?.dataset.name || 'Doctor';
    const action = btn.dataset.action;

    if (action === 'remove') {
      row.classList.add('is-leaving');
      setTimeout(() => {
        row.remove();
        showToast(`${name} was removed from the platform`, 'danger');
      }, 200);
    } else if (action === 'edit') {
      showToast(`Editing ${name} (demo only — wire this up to your edit form)`, 'success');
    }
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
  }, 2800);
}