/* Tien ich dung chung cho cac trang cua GaraMan */

/** Goi API JSON; nem loi (Error) voi thong bao tu may chu khi gap ma loi. */
async function api(path, options) {
  const opts = Object.assign({ headers: {} }, options || {});
  if (opts.body && typeof opts.body !== 'string') {
    opts.body = JSON.stringify(opts.body);
    opts.headers['Content-Type'] = 'application/json';
  }
  const res = await fetch(path, opts);
  let data = null;
  try { data = await res.json(); } catch (e) { /* khong co noi dung */ }
  if (!res.ok) {
    const err = new Error((data && data.error) || 'Có lỗi xảy ra, vui lòng thử lại');
    err.status = res.status;
    throw err;
  }
  return data;
}

/** Yeu cau da dang nhap voi vai tro role; neu chua thi chuyen ve trang dang nhap. Tra ve thong tin nguoi dung. */
async function requireRole(role) {
  try {
    const me = await api('/api/me');
    if (me.role !== role) throw new Error('role');
    renderTopbar(me);
    return me;
  } catch (e) {
    location.href = 'index.html';
    return new Promise(function () { /* dung o day cho den khi trang chuyen */ });
  }
}

function renderTopbar(me) {
  const bar = document.getElementById('topbar');
  if (!bar) return;
  bar.innerHTML = '';
  const brand = document.createElement('span');
  brand.className = 'brand';
  brand.textContent = 'GaraMan';
  const user = document.createElement('span');
  user.className = 'user';
  const name = document.createElement('span');
  name.textContent = me.fullName;
  const out = document.createElement('button');
  out.className = 'linkish';
  out.textContent = 'Đăng xuất';
  out.addEventListener('click', logout);
  user.append(name, out);
  bar.append(brand, user);
}

async function logout() {
  try { await api('/api/logout', { method: 'POST' }); } catch (e) { /* bo qua */ }
  sessionStorage.clear();
  location.href = 'index.html';
}

/** yyyy-MM-dd -> dd/MM/yyyy */
function fmtDate(iso) {
  if (!iso) return '';
  const p = iso.split('-');
  return p[2] + '/' + p[1] + '/' + p[0];
}

function fmtMoney(n) {
  return Math.round(n).toLocaleString('vi-VN');
}

function showError(el, msg) {
  el.textContent = msg;
  el.classList.remove('hidden', 'ok');
  el.classList.add('err');
}

function hide(el) { el.classList.add('hidden'); }

/** Tao the <td> an toan (textContent). */
function td(text, cls, link) {
  const c = document.createElement('td');
  if (cls) c.className = cls;
  if (link) {
    const a = document.createElement('a');
    a.href = link;
    a.textContent = text;
    c.appendChild(a);
  } else {
    c.textContent = text;
  }
  return c;
}

function todayIso() {
  const d = new Date();
  return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
}
