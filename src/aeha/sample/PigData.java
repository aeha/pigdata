package aeha.sample;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PigData extends Thread {
	/** 開始値 */
	public long start = 0L;
	/** 終了値 */
	public long end = 0L;
	/** pigdata格納用 */
	public int[] array = new int[0x10000];
	/**  */
	private Map<String, Integer> map = Collections.synchronizedMap(new HashMap<String, Integer>());

	@Override
	public synchronized void run() {
		init();
		for( long i = start; i < end; i++ ){
			if( i != 0 && i % 10000000 == 0 ){
				SimpleDateFormat sdf = new SimpleDateFormat("H:m:s");
				System.out.println( "Created " + i +" PigDatas, " + sdf.format(new Date()) );
			}
			getData(i);
		}
		System.out.println("Thread finished.");
	}


	/**
	 * getdataの実装
	 * @param num
	 */
	public void getData(long num){
		String str_sha1 = getSHA1(num);
		String str_cut = "";
		int index = 0;
		for( int i = 0; i < 10; i++ ){
			str_cut = str_sha1.substring(i * 4, i * 4 + 4 );
			index = map.get(str_cut);
			array[index]++;
		}
	}

	/**
	 * SHA-1値を得る
	 * @param num
	 * @return SHA-1値
	 */
	public String getSHA1(long num){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA"); //あるいはMD5など→アルゴリズム名
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		md.reset();
		md.update(Long.toString(num).getBytes());
		byte[] hash= md.digest();

		// ハッシュを16進数文字列に変換
		StringBuffer sb= new StringBuffer();
		int cnt= hash.length;
		for(int i= 0; i< cnt; i++){
			sb.append(Integer.toHexString( (hash[i]>> 4) & 0x0F ) );
			sb.append(Integer.toHexString( hash[i] & 0x0F ) );
		}
		return sb.toString();
	}

	/**
	 * 初期処理
	 */
	public void init(){
		String str = "";
		for(int i = 0; i <= 0xFFFF; i++){
			array[i] = 0;
			str = "000" + Integer.toHexString(i);
			str = str.substring(str.length() - 4);
			map.put(str, i);
		}
	}


}
