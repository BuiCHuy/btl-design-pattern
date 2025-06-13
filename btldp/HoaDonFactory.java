package btldp;

import java.sql.SQLException;

public abstract class HoaDonFactory {
	public abstract HoaDon TaoHoaDon(HoaDonData data);
	public void LuuHoaDon(HoaDon hd) throws SQLException {
		hd.LuuHoaDon();
	};
}
