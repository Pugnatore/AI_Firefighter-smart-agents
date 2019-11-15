package Agents;

import Agents.VehiclesAgent.ReceiveMessages;
import Messages.FireMessage;
import Messages.OrderMessage;
import Messages.StatusMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Vehicles_firetruckAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isAvailable = true;
	private int coordX = 50;
	private int coordY = 50;
	private int fireStationCoordX = 50;
	private int fireStationCoordY = 50;

	protected void setup() {
		System.out.println("Vehicle started");
		addBehaviour(new ReceiveMessages(this));
	}
	
	class ReceiveMessages extends CyclicBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ReceiveMessages(Agent a) {
			super(a);
		}

		
		public void action() {
			ACLMessage msg = receive();
			if(msg == null) {
				block();
				return;
			}
			
			try {
				Object content = msg.getContentObject();
				switch(msg.getPerformative()) {
				case (ACLMessage.REQUEST):
					if(content instanceof FireMessage) {
						FireMessage fm = (FireMessage) content;
						System.out.println("Fire msg received in vehicle. X: " + fm.getFireCoordX() + " Y: " + fm.getFireCoordY());
						//o veiculo deve:
						//verificar se est� ocupado
						//verificar se tem combustivel para se mover para as coordenadas
						//verificar se tem �gua suficiente para apagar o fogo
						StatusMessage sm = new StatusMessage(coordX, coordY, fm.getFireCoordX(), fm.getFireCoordY(), isAvailable);
						ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
						reply.setContentObject(sm);
						reply.addReceiver(msg.getSender());
						send(reply);
					}
					break;
				case (ACLMessage.PROPOSE):
					if(content instanceof OrderMessage) {
						OrderMessage om = (OrderMessage) content;
						System.out.println("Vehicle should go to " + om.getFireCoordX() + ", " + om.getFireCoordY());
					}
					break;
				default:
					break;
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		
	}
}
