import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Counter2 {

	public class SR_flipflop {
		private int x;
		private int y;

		private int q;
		private int qdash;

		SR_flipflop() {
		x = 0;
		y = 0;
		q = 0;
		qdash = 0;
		}

		public void setValues(int x,int y) {	
			this.x = x;
			this.y = y;
			if(x == 1 && y == 0) {
				q = 1;
				qdash = 0;
			}
			else if(x == 0 && y == 1) {
				q = 0;
				qdash = 1;
			}
		}

		public int getQ() {
			return q;
		}	

		public int getQdash() {
			return qdash;
		}
	}

	private JFrame frame = new JFrame("BCD Counter");

	private int ButtonWidth = 80;
	private int ButtonHeight = 30;
	private int LabelDimention = 30;

	private JLabel bit1Label = new JLabel("0");
	private JLabel bit2Label = new JLabel("0");
	private JLabel bit3Label = new JLabel("0");
	private JLabel bit4Label = new JLabel("0");
	private JLabel number    = new JLabel("0");

	private JButton count = new JButton("Count");
	private JButton reset = new JButton("Reset");

	Counter2() {
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setBackground(Color.RED);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

		count.setBounds(70,50,ButtonWidth,ButtonHeight);
		reset.setBounds(150,50,ButtonWidth,ButtonHeight);
		number.setBounds(150,120,LabelDimention,LabelDimention);
		bit1Label.setBounds(65,200,LabelDimention,LabelDimention);
		bit2Label.setBounds(115,200,LabelDimention,LabelDimention);
		bit3Label.setBounds(165,200,LabelDimention,LabelDimention);
		bit4Label.setBounds(215,200,LabelDimention,LabelDimention);


		frame.add(count);
		frame.add(reset);
		frame.add(bit1Label);
		frame.add(bit2Label);
		frame.add(bit3Label);
		frame.add(bit4Label);
		frame.add(number);

		frame.setSize(300,300);

		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Reset();
			}
		});
	}

	private SR_flipflop sr1 = new SR_flipflop();
	private SR_flipflop sr2 = new SR_flipflop();
	private SR_flipflop sr3 = new SR_flipflop();
	private SR_flipflop sr4 = new SR_flipflop();

	private int bit1;
	private int bit2;
	private int bit3;
	private int bit4;

	public int and(int x,int y) {
		if(x == 1 && y == 1)
			return 1;
		return 0;
	}

	public int not(int x) {
		if(x==1)
			return 0;
		return 1;
	}

	public void Reset() {
		
	}

	public void start() {
		count.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Count();
				show();
			}
		});
	}

	public void Count() {
		bit1 = sr1.getQ();
		bit2 = sr2.getQ();
		bit3 = sr3.getQ();
		bit4 = sr4.getQ();

		sr1.setValues(and(and(bit2,bit3),bit4),and(bit1,bit4));
		sr2.setValues(and(and(bit3,bit4),not(bit2)),and(and(bit2,bit3),bit4));
		sr3.setValues(and(and(not(bit1),not(bit3)),bit4),and(bit3,bit4));
		sr4.setValues(not(bit4),bit4);

		bit1 = sr1.getQ();
		bit2 = sr2.getQ();
		bit3 = sr3.getQ();
		bit4 = sr4.getQ();


	}

	public int BintoDec(int a,int b,int c,int d) {
		int form = 0;

		form+=a*8;
		form+=b*4;
		form+=c*2;
		form+=d*1;

		return form;
	}

	public void show() {
		bit1Label.setText(Integer.toString(bit1));
		bit2Label.setText(Integer.toString(bit2));
		bit3Label.setText(Integer.toString(bit3));
		bit4Label.setText(Integer.toString(bit4));
		number.setText(Integer.toString(BintoDec(bit1,bit2,bit3,bit4)));
		System.out.println(bit1+" "+bit2+" "+bit3+" "+bit4);
	}

	public static void main(String args[]) {
		Counter2 counter = new Counter2();
		counter.start();
	}
}