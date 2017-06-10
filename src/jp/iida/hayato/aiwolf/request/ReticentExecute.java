package jp.iida.hayato.aiwolf.request;

import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Talk;

import java.util.ArrayList;


/**
 * �s����p�u�ǖْ݂�v
 */
public final class ReticentExecute extends AbstractActionStrategy {

	@Override
	public ArrayList<Request> getRequests(ActionStrategyArgs args) {

		ArrayList<Request> Requests = new ArrayList<Request>();
		Request workReq;

		// �Ֆʐ�������ړI�̂��߁A�K�p�͒��Ղ܂łƂ���
		if( args.agi.latestGameInfo.getDay() < 5 ){

			// �l�����̐��������̉� idx=AgentNum
			int estimateNums[] = new int[ args.agi.gameSetting.getPlayerNum() + 1 ];

			// �S�Ă̔������X�g���m�F���A�l�����̐������������J�E���g����
			for( int day = 1; day < args.agi.latestGameInfo.getDay(); day++ ){
				for( Talk talk : args.agi.getTalkList(day) ){

					Utterance utterance = args.agi.getUtterance( talk.getContent() );

					switch( utterance.getTopic() ){
						case ESTIMATE:
							// �Ώۂ����݂��Ȃ���
							if( !args.agi.isValidAgentNo( utterance.getTarget().getAgentIdx() ) ){
								continue;
							}
							// �Ώۂ�����
							if( utterance.getTarget().getAgentIdx() == talk.getAgent().getAgentIdx() ){
								continue;
							}

							// �������������J�E���g
							estimateNums[talk.getAgent().getAgentIdx()]++;

						case AGREE:
							// �����̈Ӗ����擾
							Utterance refutterance = args.agi.getMeanFromAgreeTalk( talk, 0 );

							if( refutterance != null ){
								switch( refutterance.getTopic() ){
									case ESTIMATE:
										// �Ώۂ����݂��Ȃ���
										if( !args.agi.isValidAgentNo( refutterance.getTarget().getAgentIdx() ) ){
											continue;
										}
										// �Ώۂ�����
										if( refutterance.getTarget().getAgentIdx() == talk.getAgent().getAgentIdx() ){
											continue;
										}

										// �������������J�E���g
										estimateNums[talk.getAgent().getAgentIdx()]++;

									default:
										break;
								}
							}

							break;
						default:
					}
				}
			}

			// ���������̏��Ȃ��v���C���[�͉��l��������
			for( int i = 1; i <= args.agi.gameSetting.getPlayerNum(); i++ ){
				if( estimateNums[i] < args.agi.latestGameInfo.getDay() ){
					workReq = new Request( i );
					workReq.vote = 1.05;
					workReq.guard = 0.95;

					// �s���v���̓o�^
					Requests.add(workReq);
				}
			}

		}



		return Requests;

	}

}
