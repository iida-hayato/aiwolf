package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.condition.AbstractCondition;
import jp.iida.hayato.aiwolf.condition.OrCondition;
import jp.iida.hayato.aiwolf.condition.RoleCondition;
import jp.iida.hayato.aiwolf.lib.VoteAnalyzer;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Vote;

import java.util.ArrayList;
import java.util.List;

/**
 * �����u���[��v�N���X
 */
public final class VoteTarget extends AbstractGuessStrategy {

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {

		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();


		// �S�Ă̓��[�������m�F����(���񓊕[=1����)
		for( int day = 1; day < args.agi.latestGameInfo.getDay(); day++ ){

			VoteAnalyzer analyzer = new VoteAnalyzer(args.agi.getVoteList(day));


			// ��CO�҂̃��X�g���擾����
			List<Integer> mediums = args.agi.getEnableCOAgentNo(Role.MEDIUM, day+1, 0);

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

				AbstractCondition agentWolf = RoleCondition.getRoleCondition( voteAgentNo, Role.WEREWOLF );
				AbstractCondition agentPossessed = RoleCondition.getRoleCondition( voteAgentNo, Role.POSSESSED );

				Guess guess;


				// ���[��̓��[���������ꍇ�l�T�Ō���
				guess = new Guess();
				guess.condition = agentWolf;
				guess.correlation = Math.pow(0.98 + analyzer.getReceiveVoteCount(voteTargetNo) * 0.01, weight);
				guesses.add(guess);


				//TODO ���Ґ��Ή�
				// �����̐�ւ̓��[�͐l�O�Ō���
				if( day < 3 ){
					Role CORole = args.agi.getCORole( voteTargetNo, day+1, 0 );
					if( CORole == Role.SEER ){
						guess = new Guess();
						guess.condition = new OrCondition().addCondition(agentWolf).addCondition(agentPossessed);
						guess.correlation = 1.05;
						guesses.add(guess);
					}
				}

				// 1CO�̗�ւ̓��[�͐l�O�Ō���
				if( mediums.size() == 1 && mediums.get(0) == vote.getTarget().getAgentIdx() ){
					guess = new Guess();
					guess.condition = new OrCondition().addCondition(agentWolf).addCondition(agentPossessed);
					guess.correlation = 1.10;
					guesses.add(guess);
				}

			}
		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
