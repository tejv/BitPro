import org.ykc.bitpro.*;

public class BitProTestDrive {
    
	public static void main(String[] args) {
		long a = 0x6;
		long b = 0x0;
		int c = GUtils.castLongtoUInt(a);
		int d = GUtils.castLongtoUInt(b);
		
		System.out.println(Long.toHexString(b-a));
		//System.out.println((Long.toHexString(0xFFFFFFFFFFFFFFFFL - (b-a))));
		System.out.println(Integer.toHexString((d-c)));
		System.out.println(Integer.toString((d-c)));
		System.out.println(Integer.toUnsignedString((d-c)));
		//System.out.println(Integer.toHexString((c-d)));
		//System.out.println(Integer.toHexString((c-d)));
		//System.out.println(Integer.toHexString((c-d)));

	}   
}
