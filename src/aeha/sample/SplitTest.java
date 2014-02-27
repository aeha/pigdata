package aeha.sample;

public class SplitTest {

	public static final int SPLIT_CNT = 8;
	public static final long PIG_NUM = 10737418239L;
	public static final long SIG_NUM = 16777216L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println( PIG_NUM / SPLIT_CNT );
		long cnt = PIG_NUM / SPLIT_CNT;
		long total = 0;
		while(total < PIG_NUM){
			System.out.println( "start = " + total );
			total += cnt;
			if( total > PIG_NUM ){
				total = PIG_NUM;
			}
			System.out.println( "end = " + total );
		}


//		for( int i = 0; i < cnt; i++ ){
//		}


	}

}
