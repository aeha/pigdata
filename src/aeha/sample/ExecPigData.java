package aeha.sample;

import java.util.ArrayList;

public class ExecPigData extends Thread {

//	public static final long PIG_NUM = 107374182400L;
	/** 生成pigdata数 */
	public static final long PIG_NUM = 10737418240L;
	/** skip数 */
	public static final long SIG_NUM = 16777216L;
	/** 分割スレッド数 */
	public static final int SPLIT_CNT  = 16;
//	public static final long PIG_NUM = 10L;
//	public static final long SIG_NUM = 10L;



	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		long cnt = PIG_NUM / SPLIT_CNT;
		long total = 0;
		ArrayList<PigData> pig_threads = new ArrayList<PigData>();

		// 分割数ごとに、開始/終了値を計算し、pigdataを算出するスレッドを生成する
		while(total < PIG_NUM){
			PigData fork = new PigData();
			System.out.println( "start = " + total );
			fork.start = total;
			total += cnt;
			if( total > PIG_NUM ){
				total = PIG_NUM;
			}
			System.out.println( "end = " + total );
			fork.end = total;
			pig_threads.add(fork);
		}

		for (PigData fork : pig_threads) {
			fork.start();
		}

		for (PigData fork : pig_threads) {
			fork.join();
		}

		// 各スレッドで生成されたpigdataを合算
		int[] array = new int[0x10000];
		for (PigData fork : pig_threads) {
			for(int i = 0; i < fork.array.length; i++){
				array[i] += fork.array[i];
			}
		}

		// DEBUG用に、どのようなpigdata値が生成されたかを出力
		for( int i = 0x0000; i <= 0xFFFF; i++ ){
			System.out.println( "index:" + i + " = " + array[i] );
		}

		// スレッドで計算されたシグニチャより、skips値をサマリする
		long sum = 0;
		long index = 0;
		for( int i = 0x0000; i <= 0xFFFF; i++ ){
			for(long j = 0; j < array[i]; j++ ){
				if( index == 0 || index % SIG_NUM == 0 ){
					sum += i;
					System.out.println("index:" + index + " = "+ i);
				}
				index++;
			}
		}
		System.out.println( "sum = " + sum);

	}


}
