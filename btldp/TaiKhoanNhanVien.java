package btldp;


public class TaiKhoanNhanVien implements TaiKhoanNguoiDung {
    private String username;
    private String password;

    public TaiKhoanNhanVien(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() { return username; }
    @Override
    public String getPassword() { return password; }
    @Override
    public void dangNhap() { System.out.println(username + " đã đăng nhập."); }
    @Override
    public void dangXuat() { System.out.println(username + " đã đăng xuất."); }
}