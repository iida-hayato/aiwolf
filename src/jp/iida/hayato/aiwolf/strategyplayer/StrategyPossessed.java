package jp.iida.hayato.aiwolf.strategyplayer;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import jp.iida.hayato.aiwolf.lib.CauseOfDeath;
import jp.iida.hayato.aiwolf.lib.Judge;
import jp.iida.hayato.aiwolf.lib.ViewpointInfo;
import org.aiwolf.client.lib.TemplateTalkFactory;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.net.GameInfo;

import java.util.ArrayList;
import java.util.List;


public class StrategyPossessed extends AbstractBaseStrategyPlayer {


	/**
	 * �R���X�g���N�^
	 * @param agentStatistics
	 */
	public StrategyPossessed(AgentStatistics agentStatistics) {
		super(agentStatistics);
	}


	@Override
	public void dayStart() {

	    try{
			super.dayStart();

			// �x�蔻��̒ǉ�
			if( agi.latestGameInfo.getDay() > 0 ){
				addFakeSeerJudge();
			}
	    }catch(Exception ex){
	        // ��O���ăX���[����
	        if(isRethrowException){
	            throw ex;
	        }

	        // �ȉ��A��O�������̑�֏������s��(�߂�l�����郁�\�b�h�̏ꍇ�͖߂�)
	        // Do nothing
	    }

	}

	@Override
	public String talk() {

		try{
			String workString;

			// PP���̔���
			if( agi.isEnablePowerPlay_Possessed() ){
				// ���[���ύX����
				actionUI.voteAgent = planningVoteAgent;
				declaredPlanningVoteAgent = planningVoteAgent;

				// ���b
				String ret = TemplateTalkFactory.vote( Agent.getAgent(planningVoteAgent) );
				return ret;
			}

			// ��CO�̏ꍇ
			if( !isCameOut ){
				isCameOut = true;

				String ret = TemplateTalkFactory.comingout( getMe(), fakeRole );
				return ret;
			}

			// CO�ς̏ꍇ
			if( isCameOut ){

				// ���񍐂̌��ʂ�񍐂���
				if( agi.reportSelfResultCount < agi.selfInspectList.size() ){

					Judge reportJudge = agi.selfInspectList.get( agi.selfInspectList.size() - 1 );

					// �񍐍ς݂̌����𑝂₷
					agi.reportSelfResultCount++;

					// ���b
					String ret = TemplateTalkFactory
              .divined( Agent.getAgent(reportJudge.targetAgentNo), reportJudge.result );
					return ret;
				}

			}

			// ���[��������Ă��Ȃ��ꍇ�A�V�������[���b��
			if( declaredPlanningVoteAgent == null ){
				// ���[���ύX����
				actionUI.voteAgent = planningVoteAgent;
				declaredPlanningVoteAgent = planningVoteAgent;

				// �ŏI���͘T�̕֏�[��h�����ߐ錾���Ȃ��B�錾�͍ŏI���ȊO�Ƃ���i�Ƃ������ڂő��Ɠ��������킹��j
				if( agi.latestGameInfo.getAliveAgentList().size() > 4 ){
					// ���b
					String ret = TemplateTalkFactory.vote( Agent.getAgent(planningVoteAgent) );
					return ret;
				}
			}

			// �^�����T�̐l���ȏ㌾���Ă��Ȃ���Θb��
			if( agi.talkedSuspicionAgentList.size() < agi.gameSetting.getRoleNumMap().get(Role.WEREWOLF) ){
				// �^�����b�����͂��擾���A�擾�ł��Ă���Θb��
				workString = getSuspicionTalkString();
				if( workString != null ){
					return workString;
				}
			}

			// ���[���ύX����ꍇ�A�V�������[���b��
			if( declaredPlanningVoteAgent != planningVoteAgent ){
				// ���[���ύX����
				actionUI.voteAgent = planningVoteAgent;
				declaredPlanningVoteAgent = planningVoteAgent;

				// �ŏI���͘T�̕֏�[��h�����ߐ錾���Ȃ��B�錾�͍ŏI���ȊO�Ƃ���i�Ƃ������ڂő��Ɠ��������킹��j
				if( agi.latestGameInfo.getAliveAgentList().size() > 4 ){
					// ���b
					String ret = TemplateTalkFactory.vote( Agent.getAgent(planningVoteAgent) );
					return ret;
				}
			}

			// �^�����b�����͂��擾���A�擾�ł��Ă���Θb��
			workString = getSuspicionTalkString();
			if( workString != null ){
				return workString;
			}

			// �M�p���b�����͂��擾���A�擾�ł��Ă���Θb��
			workString = getTrustTalkString();
			if( workString != null ){
				return workString;
			}

			// �b�����������ꍇ�Aover��Ԃ�
			return TemplateTalkFactory.over();

		}catch(Exception ex){
	        // ��O���ăX���[����
	        if(isRethrowException){
	            throw ex;
	        }

	        // �ȉ��A��O�������̑�֏������s��(�߂�l�����郁�\�b�h�̏ꍇ�͖߂�)

			// �G���[����over��Ԃ�
			return TemplateTalkFactory.over();

		}

	}


	/** �x���E��ݒ肷�� */
	@Override
	protected void setFakeRole(){
		setFakeRole(Role.SEER);
	}


	/**
	 * �肢�����ǉ�����
	 */
	private void addFakeSeerJudge(){

		GameInfo gameInfo = agi.latestGameInfo;

		// �����E���茋�ʂ̉��ݒ�
		int inspectAgentNo = latestRequest.getMaxInspectRequest().agentNo;
		Species result = Species.HUMAN;

		// �P�����ꂽ�G�[�W�F���g�̎擾
		Agent attackedAgent = agi.latestGameInfo.getAttackedAgent();

		List<Integer> seers = agi.getEnableCOAgentNo(Role.SEER);
		List<Integer> mediums = agi.getEnableCOAgentNo(Role.MEDIUM);

		// �j�]��
		if( agi.selfViewInfo.wolfsidePatterns.isEmpty() ){
			// �����Ă��鋶���_�̊m����T��
			List<Integer> whiteList = new ArrayList<Integer>();
			for( int i = 1; i <= agi.gameSetting.getPlayerNum(); i++ ){
				if( agi.agentState[i].causeofDeath == CauseOfDeath.ALIVE && agi.selfRealRoleViewInfo.isFixWhite(i) ){
					whiteList.add(i);
				}
			}
			// �m���ɍ��o��
			if( !whiteList.isEmpty() ){
				inspectAgentNo = whiteList.get(0);
				result = Species.WEREWOLF;

				Judge newJudge = new Judge( getMe().getAgentIdx(),
                        inspectAgentNo,
                        result,
                        null );

				agi.addFakeSeerJudge(newJudge);
				return;
			}

		}

		//TODO ��U����ŏ������Ĕ��f
//		// �����͍��o���ŋ��A�s(4CO�ȉ��̏ꍇ)
//		List<Integer> seers = agi.getEnableCOAgentNo(Role.SEER);
//		List<Integer> mediums = agi.getEnableCOAgentNo(Role.MEDIUM);
//		if( agi.latestGameInfo.getDay() == 1 && seers.size() + mediums.size() <= 4 ){
//			result = Species.WEREWOLF;
//		}


//		// �����\�ɍ��o��
//		if( agi.latestGameInfo.getDay() == 1 && mediums.size() == 1 ){
//			for( Integer medium : mediums ){
//				if( agi.agentState[medium].causeofDeath == CauseOfDeath.ALIVE &&
//					!agi.selfViewInfo.isFixWhite(medium) &&
//					!agi.selfViewInfo.isFixBlack(medium) ){
//					inspectAgentNo = medium;
//					result = Species.WEREWOLF;
//				}
//			}
//		}
//
//		// �΍R�ɍ��o��
//		if( agi.latestGameInfo.getDay() > 1 && seers.size() == 2 ){
//			for( Integer seer : seers ){
//				if( agi.agentState[seer].causeofDeath == CauseOfDeath.ALIVE &&
//					!agi.selfViewInfo.isFixWhite(seer) &&
//					!agi.selfViewInfo.isFixBlack(seer) ){
//					inspectAgentNo = seer;
//					result = Species.WEREWOLF;
//				}
//			}
//		}

//		// ��\�ɍ��o��
//		if( agi.latestGameInfo.getDay() == 2 && mediums.size() == 1 ){
//			for( Integer medium : mediums ){
//				if( agi.agentState[medium].causeofDeath == CauseOfDeath.ALIVE &&
//					!agi.selfViewInfo.isFixWhite(medium) &&
//					!agi.selfViewInfo.isFixBlack(medium) ){
//					inspectAgentNo = medium;
//					result = Species.WEREWOLF;
//				}
//			}
//		}
//
//		// �΍R�ɍ��o��
//		if( agi.latestGameInfo.getDay() == 1 && seers.size() == 2 ){
//			for( Integer seer : seers ){
//				if( agi.agentState[seer].causeofDeath == CauseOfDeath.ALIVE &&
//					!agi.selfViewInfo.isFixWhite(seer) &&
//					!agi.selfViewInfo.isFixBlack(seer) ){
//					inspectAgentNo = seer;
//					result = Species.WEREWOLF;
//				}
//			}
//		}



		// �����o�����ꍇ�̎��_�����肷��
		ViewpointInfo future = new ViewpointInfo(agi.selfViewInfo);
		future.removeWolfPattern(inspectAgentNo);

		// ���o���œ��󂪔j�]����ꍇ�A���o�����s��
		if( future.wolfsidePatterns.isEmpty() ){
			result = Species.WEREWOLF;
		}


		// �肨���Ƃ����悪���܂ꂽ
		if( attackedAgent != null && attackedAgent.getAgentIdx() == inspectAgentNo ){
			// ���ݐ�ɂ͐l�Ԕ�����o��
			result = Species.HUMAN;
		}


		// ��\�����S
		boolean mediumAlive = false;
		if( !mediums.isEmpty() ){
			for( int medium : mediums ){
				if( agi.agentState[medium].causeofDeath == CauseOfDeath.ALIVE ){
					// 1�l�ł������Ă�ΐ���������
					mediumAlive = true;
				}else{
					// ���S�����삪�^�m�肩
					if( agi.selfRealRoleViewInfo.isFixWhite(medium) ){
						mediumAlive = false;
						break;
					}
				}
			}
			// ��\���S��
			if( mediumAlive == false ){
				// 40%�Ŕ��ɍ��o��
				if( Math.random() < 0.4 ){
					// �����Ă���m����T��
					List<Integer> whiteList = new ArrayList<Integer>();
					for( int i = 1; i <= agi.gameSetting.getPlayerNum(); i++ ){
						if( agi.agentState[i].causeofDeath == CauseOfDeath.ALIVE && agi.selfRealRoleViewInfo.isFixWhite(i) ){
							whiteList.add(i);
						}
					}

					// ���������_�̊m���ɍ����o��
					for( Integer white : whiteList ){
						// �����莋�_�ŐF���m�肵�Ă���ꍇ�͐��Ȃ�
						if( agi.selfViewInfo.isFixWhite(white) ||
						    agi.selfViewInfo.isFixBlack(white) ){
							continue;
						}

						// �����o�����ꍇ�̎��_�����肷��
						future = new ViewpointInfo(agi.selfViewInfo);
						future.removePatternFromJudge( getMe().getAgentIdx(), white, Species.WEREWOLF );

						// �����o���Ĕj�]���Ȃ��Ȃ炻���ɍ����o��
						if( !future.wolfsidePatterns.isEmpty() ){
							inspectAgentNo = white;
							result = Species.WEREWOLF;
							break;
						}
					}
				}
			}
		}



		// �����҂T���ȉ��i�����P�l�݂��PP�m��j
		if( gameInfo.getAliveAgentList().size() <= 5 ){

			// �����Ă���m����T��
			List<Integer> whiteList = new ArrayList<Integer>();
			for( int i = 1; i <= agi.gameSetting.getPlayerNum(); i++ ){
				if( agi.agentState[i].causeofDeath == CauseOfDeath.ALIVE && agi.selfRealRoleViewInfo.isFixWhite(i) ){
					whiteList.add(i);
				}
			}

			// ���������_�̊m���ɍ����o��
			for( Integer white : whiteList ){
				// �����莋�_�ŐF���m�肵�Ă���ꍇ�͐��Ȃ�
				if( agi.selfViewInfo.isFixWhite(white) ||
				    agi.selfViewInfo.isFixBlack(white) ){
					continue;
				}

				// �����o�����ꍇ�̎��_�����肷��
				future = new ViewpointInfo(agi.selfViewInfo);
				future.removePatternFromJudge( getMe().getAgentIdx(), white, Species.WEREWOLF );

				// �����o���Ĕj�]���Ȃ��Ȃ炻���ɍ����o��
				if( !future.wolfsidePatterns.isEmpty() ){
					inspectAgentNo = white;
					result = Species.WEREWOLF;
					break;
				}
			}

		}

		// �m��ƃ��C���������Ȃ画��𔽓]����
		if( mediums.size() == 1 &&  agi.agentState[mediums.get(0)].causeofDeath == CauseOfDeath.ALIVE ){
			if( result == Species.HUMAN ){
				// �����o�����ꍇ�̎��_�����肷��
				future = new ViewpointInfo(agi.selfViewInfo);
				future.removeWolfPattern(inspectAgentNo);
				// �������_�ŗ�\���m��l�O�Ȃ烉�C��������邽�ߔ��蔽�]
				if( future.isFixWolfSide(mediums.get(0)) ){
					result = Species.WEREWOLF;
				}
			}else{
				// �����o�����ꍇ�̎��_�����肷��
				future = new ViewpointInfo(agi.selfViewInfo);
				future.removePatternFromJudge(agi.latestGameInfo.getAgent().getAgentIdx(), inspectAgentNo, Species.WEREWOLF);
				// �������_�ŗ�\���m��l�O�Ȃ烉�C��������邽�ߔ��蔽�]
				if( future.isFixWolfSide(mediums.get(0)) ){
					result = Species.HUMAN;
				}
			}
		}

		Judge newJudge = new Judge( getMe().getAgentIdx(),
		                            inspectAgentNo,
		                            result,
		                            null );

		agi.addFakeSeerJudge(newJudge);

	}



}
