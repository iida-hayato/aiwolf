package jp.iida.hayato.aiwolf.learn;

public final class MyAgentStatistics {


	/** �Q�[���� */
	public static int gameCount;


	/** �Q�[����(���w�c) */
	public static int villagerSide_gameCount;

	/** �Q�[����(�T�w�c) */
	public static int wolfSide_gameCount;


	/** ������ */
	public static int aliveCount;

	/** ���Y�� */
	public static int executeCount;

	/** ��P���� */
	public static int attackCount;



	/** �ŏI�����ŘT���� idx=���������T�̐� */
	public static int[] wolfCorrectCount = new int[10];

	/** update()�̍Œ����� */
	public static long maxUpdateTime;


	/** �f�o�b�O�p */
	public static StringBuilder debugStringBuilder = new StringBuilder();


	/**
	 * �w�K�f�[�^�̉�ʏo��
	 */
	public static void printData(){

		System.out.println("�Q�[����:" + gameCount);
		//System.out.println("����:" + "/" + villagerSide_gameCount + "��");
		//System.out.println("�T��:" + "/" + wolfSide_gameCount + "��");
		System.out.println("���H:" + "(����:" + aliveCount + " �P��:" + attackCount + " ���Y:" + executeCount + ")");
		System.out.println("�T����:" + "(3:" + wolfCorrectCount[3] + " 2:" + wolfCorrectCount[2] + " 1:" + wolfCorrectCount[1] + " 0:" + wolfCorrectCount[0] + ")");

		System.out.println("update()�Œ�:" + maxUpdateTime + "ms");

		if( debugStringBuilder.length() > 0 ){
			System.out.println(debugStringBuilder.toString());
		}

	}


}
