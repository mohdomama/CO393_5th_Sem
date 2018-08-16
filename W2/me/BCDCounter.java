import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BCDCounter {
	private SrFlipFlop[] ff = new SrFlipFlop[4];
	private Boolean[] bits = new Boolean[4];
	

	BCDCounter() {
		for (int i = 0; i < 4 ; i++) {
			ff[i] = new SrFlipFlop();
			bits[i] = ff[i].getQ();
		}
	}

	public void print() {
		for (int i = 3; i >= 0 ; i--) {
			if (bits[i])
				System.out.print(1 + " ");
			else
				System.out.print(0 + " ");
		}
		System.out.println();
	}

	public void count() {
		ff[0].operate(!bits[0], bits[0]);

		ff[1].operate( 
			( (!bits[3]) & (!bits[1]) ) & bits[0],
			 bits[1] & bits[0]
			);
		
		ff[2].operate(
			(bits[1] & bits[0]) & !bits[2],
			bits[2] & bits[1] & bits[0]
			);
		
		ff[3].operate(
			bits[2] & bits[1] & bits[0],
			bits[3] & bits[0]
 			);

		for (int i = 0; i < 4 ; i++) {
			bits[i] = ff[i].getQ();
		}
	}

	public static void main(String[] args) {
		BCDCounter bcdCounter = new BCDCounter();
		Gui gui = new Gui(bcdCounter);
		JFrame frame = gui.draw();		
	}	
}

class SrFlipFlop {
	private Boolean s,r,q;

	SrFlipFlop() {
		s = true;
		r = true;
		q = false;
	}

	public void operate(Boolean s, Boolean r) {
		this.s = s;
		this.r = r;

		if (s && !r) 
			q = true;

		if (!s && r) 
			q = false;

		//else if (!s && !r) 
		//	System.out.println("Unstable");
	}

	public Boolean getQ() {
		return q;
	}
}

class Gui {

	private JFrame frame;

	private JLabel bit1;
	private JLabel bit2; 
	private JLabel bit3; 
	private JLabel bit4; 
	private JLabel decimal;
	private JLabel tagDec;
	private JLabel tagBin;

	private JButton counter;
	private JButton reset;

	private BCDCounter bcdCounter;

	private int buttonL = 100;
	private int buttonH = 30;
	private int labelSize = 50;

	Gui(BCDCounter bcdCounter) {
		frame = new JFrame("BCD Counter");

		bit1 = new JLabel("0");
		bit2 = new JLabel("0");
		bit3 = new JLabel("0");
		bit4 = new JLabel("0");
		decimal = new JLabel("0");
		tagDec = new JLabel("Decimal:");
		tagBin = new JLabel("BCD:");

		counter = new JButton("Count");
		reset = new JButton("Reset");

		this.bcdCounter = bcdCounter;
	}

	public JFrame draw() {
		counter.setBounds(70,50,buttonL,buttonH);
		reset.setBounds(70,100,buttonL,buttonH);

		tagDec.setBounds(300, 50, buttonL, buttonH);
		decimal.setBounds(300,100,labelSize,labelSize);

		tagBin.setBounds(250, 350, buttonL, buttonH);
		bit1.setBounds(100,400,labelSize,labelSize);
		bit2.setBounds(200,400,labelSize,labelSize);
		bit3.setBounds(300,400,labelSize,labelSize);
		bit4.setBounds(400,400,labelSize,labelSize);
		

		frame.setLayout(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setLocationRelativeTo(null);
		
		frame.add(bit1);
		frame.add(bit2);
		frame.add(bit3);
		frame.add(bit4);

		frame.add(tagDec);
		frame.add(tagBin);
		frame.add(decimal);

		frame.add(counter);
		frame.add(reset);
		
		counter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				countAction();
			}
		});

		frame.setVisible(true);



		return frame;
	}

	public void countAction() {
		bcdCounter.count();
		bcdCounter.print();
	}
}
