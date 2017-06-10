package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.condition.AbstractCondition;
import jp.iida.hayato.aiwolf.condition.AndCondition;
import jp.iida.hayato.aiwolf.condition.RoleCondition;
import jp.iida.hayato.aiwolf.lib.CauseOfDeath;
import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Vote;

import java.util.ArrayList;
import java.util.List;

/**
 * �����u���[�����v�N���X
 */
public final class VoteRecent extends AbstractGuessStrategy {

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {

		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();


		// �S�Ă̓��[�������m�F����(���񓊕[=1����)
		for( int day = 1; day < args.agi.latestGameInfo.getDay(); day++ ){

			// ��CO�҂̃��X�g���擾����
			List<Integer> mediums = args.agi.getEnableCOAgentNo(Role.MEDIUM, day, 0);

			for( Vote vote : args.agi.getVoteList(day) ){

				int voteAgentNo = vote.getAgent().getAgentIdx();
				int voteTargetNo = vote.getTarget().getAgentIdx();

				// ���[�̐����v�f�Ƃ��Ă̏d��(�菇�݂肾�ƌy���Ȃ�)
				double weight = 1.0;

				// �퓊�[�҂�����̏ꍇ�A�菇�݂�Ƃ��ďd�݂�������
				if( mediums.size() >= 2 && mediums.indexOf(vote.getTarget().getAgentIdx()) != -1 ){
					weight *= 0.5;
				}
				// �퓊�[�҂����Ⴂ�̏ꍇ�A�菇�݂�Ƃ��ďd�݂�������
				if( args.agi.isReceiveWolfJudge(vote.getTarget().getAgentIdx(), day, 0) ){
					weight *= 0.5;
				}

				//TODO ������@�𒚔J�ɂ���
				// ���_�m��l�O�̏ꍇ�A�d�݂�������
				Role agentRole = args.agi.getCORole(voteAgentNo, day, 0);
				Role targetRole = args.agi.getCORole(voteTargetNo, day, 0);
				if( agentRole != null && targetRole != null && agentRole == targetRole ){
					weight *= 0.3;
				}

				AbstractCondition agentWolf = RoleCondition.getRoleCondition( voteAgentNo, Role.WEREWOLF );
				AbstractCondition agentPossessed = RoleCondition.getRoleCondition( voteAgentNo, Role.POSSESSED );
				AbstractCondition targetWolf = RoleCondition.getRoleCondition( voteTargetNo, Role.WEREWOLF );
				AbstractCondition targetNotWolf = RoleCondition.getNotRoleCondition( voteTargetNo, Role.WEREWOLF );

				Guess guess;
				// �T���T�̃p�^�[���𔖂�����i���C���؂���j
				if( args.agi.agentState[voteAgentNo].causeofDeath != CauseOfDeath.ATTACKED &&
				    args.agi.agentState[voteTargetNo].causeofDeath != CauseOfDeath.ATTACKED){
					guess = new Guess();
					guess.condition = new AndCondition().addCondition(agentWolf).addCondition(targetWolf);
					guess.correlation = 1.0 - 0.4 * weight;
					guesses.add(guess);
				}

				// �T����T�̃p�^�[����Z������i�X�P�[�v�S�[�g�j
				if( args.agi.agentState[voteAgentNo].causeofDeath != CauseOfDeath.ATTACKED ){
					guess = new Guess();
					guess.condition = new AndCondition().addCondition(agentWolf).addCondition(targetNotWolf);
					guess.correlation = 1.0 + 0.020 * weight;
					guesses.add(guess);
				}

				// ������T�̃p�^�[����Z������i�X�P�[�v�S�[�g�j
				guess = new Guess();
				guess.condition = new AndCondition().addCondition(agentPossessed).addCondition(targetNotWolf);
				guess.correlation = 1.0 + 0.005 * weight;
				guesses.add(guess);

			}
		}

		// �R���ڂ��瓊�[�錾����̃��C���؂ꐄ�����s���i�R���ڂ���Ȃ̂͏������ԑ΍�j
		if( args.agi.latestGameInfo.getDay() >= 3 ){

			// ��CO�҂̃��X�g���擾����
			List<Integer> mediums = args.agi.getEnableCOAgentNo(Role.MEDIUM, args.agi.latestGameInfo.getDay(), 0);

			VoteAnalyzer saidVote = VoteAnalyzer.loadSaidVote(args.agi);

			for( Vote vote : saidVote.voteList ){
				if( vote.getTarget() != null ){

					// ���[�̐����v�f�Ƃ��Ă̏d��(�菇�݂肾�ƌy���Ȃ�)
					double weight = 1.0;

					// �퓊�[�҂�����̏ꍇ�A�菇�݂�Ƃ��ďd�݂�������
					if( mediums.size() >= 2 && mediums.indexOf(vote.getTarget().getAgentIdx()) != -1 ){
						weight *= 0.5;
					}
					// �퓊�[�҂����Ⴂ�̏ꍇ�A�菇�݂�Ƃ��ďd�݂�������
					if( args.agi.isReceiveWolfJudge(vote.getTarget().getAgentIdx(), args.agi.latestGameInfo.getDay(), 0) ){
						weight *= 0.5;
					}

					AbstractCondition agentWolf = RoleCondition.getRoleCondition( vote.getAgent(), Role.WEREWOLF );
					AbstractCondition targetWolf = RoleCondition.getRoleCondition( vote.getTarget(), Role.WEREWOLF );

					Guess guess = new Guess();
					guess.condition = new AndCondition().addCondition(agentWolf).addCondition(targetWolf);
					guess.correlation = 1.0 - 0.4 * weight;
					guesses.add(guess);
				}
			}
		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
