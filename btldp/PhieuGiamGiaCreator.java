package btldp;

import java.util.Date;

public abstract class PhieuGiamGiaCreator {
    public abstract PhieuGiamGia createPhieuGiamGia(String ma, String tenChuongTrinh, double mucGiam, Date ngayBD, Date ngayKT, String trangThai);
    public void someOperation(PhieuGiamGia ph ,double gia){
    	ph.tinhGiamGia(gia);		
    }
    
    
}