package jp.iida.hayato.aiwolf.strategyplayer;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import jp.iida.hayato.aiwolf.lib.Judge;
import org.aiwolf.client.lib.TemplateTalkFactory;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;


public class StrategySeer extends AbstractBaseStrategyPlayer {


	/**
	 * コンストラクタ
	 * @param agentStatistics
	 */
	public StrategySeer(AgentStatistics agentStatistics) {
		super(agentStatistics);
	}

	@Override
	public String talk() {

		try{

			String workString;

			// ��CO�̏ꍇ
			if( !isCameOut ){
				// CO����
				isCameOut = true;

				// ���b
				workString = TemplateTalkFactory.comingout(additionalInfo.getMe(), Role.SEER);
				return workString;
			}

			// CO�ς̏ꍇ
			if( isCameOut ){

				// ���񍐂̌��ʂ�񍐂���
				if( agi.reportSelfResultCount < agi.selfInspectList.size() ){

					Judge reportJudge = agi.selfInspectList.get( agi.selfInspectList.size() - 1 );

					// �񍐍ς݂̌����𑝂₷
					agi.reportSelfResultCount++;

					// ���b
					workString = TemplateTalkFactory
              .divined( Agent.getAgent(reportJudge.targetAgentNo), reportJudge.result );
					return workString;
				}

			}

			// ���[��������Ă��Ȃ��ꍇ�A�V�������[���b��
			if( declaredPlanningVoteAgent == null ){
				// ���[���ύX����
				actionUI.voteAgent = planningVoteAgent;
				declaredPlanningVoteAgent = planningVoteAgent;

				// �ŏI���͘T�̕֏�[��h�����ߐ錾���Ȃ��B�錾�͍ŏI���ȊO�Ƃ���
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

				// �ŏI���͘T�̕֏�[��h�����ߐ錾���Ȃ��B�錾�͍ŏI���ȊO�Ƃ���
				if( agi.latestGameInfo.getAliveAgentList().size() > 4 ){
					// ���b
					String ret = TemplateTalkFactory.vote( Agent.getAgent(planningVoteAgent) );
					return ret;
				}
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


}
