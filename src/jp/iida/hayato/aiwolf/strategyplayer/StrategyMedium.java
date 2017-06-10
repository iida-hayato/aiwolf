package jp.iida.hayato.aiwolf.strategyplayer;

import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import jp.iida.hayato.aiwolf.lib.Judge;
import org.aiwolf.client.lib.Content;
import org.aiwolf.client.lib.IdentContentBuilder;
import org.aiwolf.client.lib.TemplateTalkFactory;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;

import java.util.List;


public class StrategyMedium extends AbstractBaseStrategyPlayer {


  /**
   * �R���X�g���N�^
   */
  public StrategyMedium(AgentStatistics agentStatistics) {
    super(agentStatistics);
  }


  @Override
  public String talk() {

    try {

      String workString;

      // ��CO�̏ꍇ
      if (!isCameOut) {

        //			// ���CO���K�v�Ȃ���CO����
        //			if( isAvoidance() ){
        //				isCameOut = true;
        //
        //				// ���b
        //				workString = TemplateTalkFactory.comingout(getMe(), Role.MEDIUM);
        //				return workString;
        //			}
        //
        //			// ����CO���K�v�Ȃ�CO����
        //			if( isVoluntaryComingOut() ){
        //				isCameOut = true;
        //
        //				// ���b
        //				workString = TemplateTalkFactory.comingout(getMe(), Role.MEDIUM);
        //				return workString;
        //			}

        // CO����
        isCameOut = true;

        // ���b
        workString = TemplateTalkFactory.comingout(additionalInfo.getMe(), Role.MEDIUM);
        return workString;
      }

      // CO�ς̏ꍇ
      if (isCameOut) {

        // ���񍐂̌��ʂ�񍐂���
        if (agi.reportSelfResultCount < agi.selfInquestList.size()) {

          Judge reportJudge = agi.selfInquestList.get(agi.selfInquestList.size() - 1);

          // �񍐍ς݂̌����𑝂₷
          agi.reportSelfResultCount++;

          // ���b
          workString = new Content(
              new IdentContentBuilder(Agent.getAgent(reportJudge.targetAgentNo),
                  reportJudge.result)).getText();

          return workString;
        }

      }

      // ���[��������Ă��Ȃ��ꍇ�A�V�������[���b��
      if (declaredPlanningVoteAgent == null) {
        // ���[���ύX����
        actionUI.voteAgent = planningVoteAgent;
        declaredPlanningVoteAgent = planningVoteAgent;

        // �ŏI���͘T�̕֏�[��h�����ߐ錾���Ȃ��B�錾�͍ŏI���ȊO�Ƃ���
        if (agi.latestGameInfo.getAliveAgentList().size() > 4) {
          // ���b
          String ret = TemplateTalkFactory.vote(Agent.getAgent(planningVoteAgent));
          return ret;
        }
      }

      // �^�����T�̐l���ȏ㌾���Ă��Ȃ���Θb��
      if (agi.talkedSuspicionAgentList.size() < agi.gameSetting.getRoleNumMap()
          .get(Role.WEREWOLF)) {
        // �^�����b�����͂��擾���A�擾�ł��Ă���Θb��
        workString = getSuspicionTalkString();
        if (workString != null) {
          return workString;
        }
      }

      // ���[���ύX����ꍇ�A�V�������[���b��
      if (declaredPlanningVoteAgent != planningVoteAgent) {
        // ���[���ύX����
        actionUI.voteAgent = planningVoteAgent;
        declaredPlanningVoteAgent = planningVoteAgent;

        // �ŏI���͘T�̕֏�[��h�����ߐ錾���Ȃ��B�錾�͍ŏI���ȊO�Ƃ���
        if (agi.latestGameInfo.getAliveAgentList().size() > 4) {
          // ���b
          String ret = TemplateTalkFactory.vote(Agent.getAgent(planningVoteAgent));
          return ret;
        }
      }

      // �M�p���b�����͂��擾���A�擾�ł��Ă���Θb��
      workString = getTrustTalkString();
      if (workString != null) {
        return workString;
      }

      // �b�����������ꍇ�Aover��Ԃ�
      return TemplateTalkFactory.over();

    } catch (Exception ex) {
      // ��O���ăX���[����
      if (isRethrowException) {
        throw ex;
      }

      // �ȉ��A��O�������̑�֏������s��(�߂�l�����郁�\�b�h�̏ꍇ�͖߂�)

      // �G���[����over��Ԃ�
      return TemplateTalkFactory.over();

    }


  }


  /**
   * �����I��CO���邩
   */
  private boolean isVoluntaryComingOut() {

    // �e��E��CO�҂��擾
    List<Integer> seers = agi.getEnableCOAgentNo(Role.SEER);
    List<Integer> mediums = agi.getEnableCOAgentNo(Role.MEDIUM);
    List<Integer> bodyguards = agi.getEnableCOAgentNo(Role.BODYGUARD);

    // 2����
    if (gameInfo.getDay() >= 2) {
      // CO����
      return true;
    }

    // ���x�肪����
    if (!mediums.isEmpty()) {
      // CO����
      return true;
    }

    // �S�I�o
    if (seers.size() >= 5 ||
        bodyguards.size() >= 5 ||
        seers.size() + bodyguards.size() >= 6) {
      // CO����
      return true;
    }

    // ���ꂽ�i���Ȃ�CCO�A���Ȃ犚�܂�₷���̂ŏ�����h�~�j
    for (Judge judge : agi.getSeerJudgeList()) {
      if (judge.isEnable() && judge.targetAgentNo == additionalInfo.getMe().getAgentIdx()) {
        // CO����
        return true;
      }
    }

    return false;

  }

}
