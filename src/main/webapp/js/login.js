/* Dang nhap (UC Login): chuyen den trang chu theo vai tro */
const HOME = { CUSTOMER: 'GDHomeCus6.html', MANAGER: 'GDHomeManager6.html' };

document.getElementById('loginForm').addEventListener('submit', async function (e) {
  e.preventDefault();
  const msg = document.getElementById('msg');
  hide(msg);
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;
  if (!username || !password) {
    showError(msg, 'Vui lòng nhập tên đăng nhập và mật khẩu');
    return;
  }
  try {
    const me = await api('/api/login', { method: 'POST', body: { username: username, password: password } });
    if (!HOME[me.role]) {
      await api('/api/logout', { method: 'POST' });
      showError(msg, 'Tài khoản này chưa có giao diện trong phiên bản hiện tại');
      return;
    }
    location.href = HOME[me.role];
  } catch (err) {
    showError(msg, err.message);
  }
});
