function initToast() {
  var el = document.getElementById('toast');
  if (!el || !el.textContent.trim()) return;
  el.classList.add('show');
  setTimeout(function () { el.classList.remove('show'); }, 2600);
}
document.addEventListener('DOMContentLoaded', initToast);
