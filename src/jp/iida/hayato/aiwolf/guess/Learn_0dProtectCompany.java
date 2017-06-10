package jp.iida.hayato.aiwolf.guess;

import jp.iida.hayato.aiwolf.condition.AndCondition;
import jp.iida.hayato.aiwolf.condition.OrCondition;
import jp.iida.hayato.aiwolf.condition.RoleCondition;
import jp.iida.hayato.aiwolf.learn.AgentStatistics;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * �����u�O���ڐg���݂��v�N���X
 */
public final class Learn_0dProtectCompany extends AbstractGuessStrategy {

	AgentStatistics agentStatistics;

	public Learn_0dProtectCompany(AgentStatistics agentStatistics){
		this.agentStatistics = agentStatistics;
	}

	@Override
	public ArrayList<Guess> getGuessList(GuessStrategyArgs args) {
		// �������X�g
		ArrayList<Guess> guesses = new ArrayList<Guess>();


		// 0���ڂ͍s��Ȃ�
		if( args.agi.latestGameInfo.getDay() == 0 ){
			return guesses;
		}


		for( int agentNo = 1; agentNo <= args.agi.gameSetting.getPlayerNum(); agentNo++ ){

			// �����\�z������
			HashSet<Integer> protectAgent = new HashSet<Integer>();

			boolean isSaidVillager = false;
			for( Talk talk : args.agi.getTalkList(0) ){

				if( talk.getAgent().getAgentIdx() == agentNo ){
					Utterance ut = args.agi.getUtterance(talk.getContent());
					if( ut.getTopic() == Topic.ESTIMATE  ){
						if( args.agi.latestGameInfo.getAgentList().contains(ut.getTarget()) ){
							if( ut.getRole().getTeam() == Team.VILLAGER ) {
								protectAgent.add(ut.getTarget().getAgentIdx());
							}
						}
					}
				}
			}

			//TODO ���Ґ��Ή�
			// ���\�z��1�`12�l�̏ꍇ�ɐ������s���i13�l���ƈ͂���100%�j
			if( !protectAgent.isEmpty() && protectAgent.size() <= 12 ){

				int estimateVillagerCount = agentStatistics.statistics.get(agentNo).eventCount.get(Role.WEREWOLF).getOrDefault("Said0dEstimateVillager", 0);
				int protectCompanyCount = agentStatistics.statistics.get(agentNo).eventCount.get(Role.WEREWOLF).getOrDefault("0dProtectCompany", 0);

				// ��񂪂T���ȏ゠��ΐ������s��
				if( estimateVillagerCount > 5 ){
					// �P�T�������Ŕ݂��ĂȂ���������
					double measurementNotProtectRate = 1.0 - (double)protectCompanyCount / estimateVillagerCount;

					// �݂��ĂȂ��m���̗��_�l
					double theoreticalNotProtectRate = 1.0;
					for( int i = 0; i < protectAgent.size(); i++ ){
						theoreticalNotProtectRate *= ( (double)(12 - i)  / (14 - i) );
					}

					// �݂��ĂȂ������̗��_�l�ɑ΂���{��
					double rate = measurementNotProtectRate / theoreticalNotProtectRate;

					if( rate > 1.0 ){
						// �݂��ĂȂ��{��������
						RoleCondition wolfCondition = RoleCondition.getRoleCondition( agentNo, Role.WEREWOLF );
						OrCondition subCondition = new OrCondition();
						for( Integer i : protectAgent ){
							subCondition.addCondition(RoleCondition.getRoleCondition( i, Role.WEREWOLF ));
						}
						Guess guess = new Guess();
						guess.condition = new AndCondition().addCondition(wolfCondition).addCondition(subCondition);
						guess.correlation = Math.pow( Math.max(1.0 / rate, 0.5) , 1.0 );
						guesses.add(guess);
					}else{
						// �݂��Ă�{��������
						RoleCondition wolfCondition = RoleCondition.getRoleCondition( agentNo, Role.WEREWOLF );
						AndCondition subCondition = new AndCondition();
						for( Integer i : protectAgent ){
							subCondition.addCondition(RoleCondition.getNotRoleCondition( i, Role.WEREWOLF ));
						}
						Guess guess = new Guess();
						guess.condition = new AndCondition().addCondition(wolfCondition).addCondition(subCondition);
						guess.correlation = Math.pow( Math.max(rate, 0.5) , 1.0 );
						guesses.add(guess);
					}

				}

			}

		}

		// �������X�g��Ԃ�
		return guesses;
	}

}
