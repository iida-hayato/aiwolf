package jp.iida.hayato.aiwolf.lib;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

import java.util.List;


/**
 * �����Ƃ̏���\���N���X
 */
public final class DayInfo {

	/** ���� */
	public final int day;

	/** �����҈ꗗ */
	public final List<Agent> aliveAgentList;

	/** ���Y�҂̃G�[�W�F���g�ԍ�(�������ꂽ��) */
	public final Integer executeAgentNo;

	/** ��P���҂̃G�[�W�F���g�ԍ�(�������ꂽ��) */
	public final Integer attackAgentNo;



	public DayInfo(GameInfo gameInfo){

		day = gameInfo.getDay();

		aliveAgentList = gameInfo.getAliveAgentList();

		if( gameInfo.getAttackedAgent() != null ){
			attackAgentNo = gameInfo.getAttackedAgent().getAgentIdx();
		}else{
			attackAgentNo = null;
		}

		if( gameInfo.getExecutedAgent() != null ){
			executeAgentNo = gameInfo.getExecutedAgent().getAgentIdx();
		}else{
			executeAgentNo = null;
		}

	}



}
