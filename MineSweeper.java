import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import static javax.swing.JOptionPane.showMessageDialog;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

class MineSweeper{
	
	
	public static void main(String args[]) {	
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI(15);
			}
		});
	}

	private static void createGUI(int mode) {
		AtomicBoolean gameLost = new AtomicBoolean(false);
		ImageIcon[] images = new ImageIcon[] {
			new ImageIcon(Class.class.getResource("/resources/bomb.png")),
			new ImageIcon(Class.class.getResource("/resources/redflag.png"))
			
		};
		//get mask and minesweeper board from mineContainer
		mineContainer mC = new mineContainer();
		mC.bomb(mode); // todo, user difficulty.


		boolean[][] mask = mC.mask;
		int[][]  board = mC.board;
		JButton[][] mineBoardSquares = new JButton[12][12]; // create board.
		JFrame frame;
		JPanel mineBoard;
		
		//create frame
		frame = new JFrame("MineSweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,400);


		//creating the MenuBar and adding components
		JMenuBar menubar = new JMenuBar();
		JMenu diff = new JMenu("Difficulty");
		menubar.add(diff); // add diff to menubar
		menubar.add(Box.createHorizontalStrut( 70 )); // 70 pixel space betwen next value
		JMenuItem easy = new JMenuItem("Easy");
		JMenuItem medium = new JMenuItem("Medium");
		JMenuItem hard = new JMenuItem("Hard");

		//game reset button
		JButton reset = new JButton(new ImageIcon(Class.class.getResource("/resources/smileyface.png")));
		menubar.add(reset);
		menubar.add(Box.createHorizontalStrut( 50 )); // 50 pixel space betwen next value



		//flag button
		AtomicInteger flagSwitch = new AtomicInteger(0); // atomic integer to keep track of bomb/flag mode.

		JButton flag = new JButton(images[flagSwitch.get()]);
		menubar.add(flag);
		menubar.add(Box.createHorizontalStrut( 30 )); // 50 pixel space betwen next value

		//jlabel to keep count of how many bombs left.
		AtomicInteger bombsActive = new AtomicInteger(0); // atomic integer to keep track of bombs, active. mode - flagged positions.
		JLabel jlabel = new JLabel("   " + mode  + "   ");
		jlabel.setForeground(Color.RED);
		jlabel.setBackground(Color.black);
		jlabel.setOpaque(true);
		menubar.add(jlabel);



		//give action listeners to menubar
		//reset action listener
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createGUI(mode);
				frame.dispose();
			}});

		// flag actionlistener
		flag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flag.setIcon(images[flagSwitch.incrementAndGet() % images.length]);
			}});


		easy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createGUI(15);
				frame.dispose();
			}
		});

		medium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createGUI(30);
				frame.dispose();
			}
		});

		hard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createGUI(40);
				frame.dispose();
			}
		});

		//add to menu
		diff.add(easy);
		diff.add(medium);
		diff.add(hard);


		//create the mineBoard
		mineBoard = new JPanel(new GridLayout(0,12));
		mineBoard.setBorder(new LineBorder(Color.WHITE));

		//add the mineBoard to frame
		frame.add(mineBoard);

		Insets buttonMargin = new Insets(0,0,0,0);
		//creation of mineboard in swing, add actionlisteners to each jbutton.
		for (int i  = 0; i < mineBoardSquares.length; i++) {
			for (int j = 0; j < mineBoardSquares[0].length; j++) {
				JButton b = new JButton();
				b.setName("clear");
				//change the text based on if button is pressed
				//this activates move. then goes through every value 
				final int ii = i, jj = j;
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (gameLost.get()) return;
						//normal minesweeper mode
						if (flagSwitch.get() % 2 == 0 && b.getName() == "clear") {
							//player hits a bomb.
							if (board[ii][jj] == -1) {
								reset.setIcon(new ImageIcon(Class.class.getResource("/resources/deadbomb.png")));
								mineBoardSquares[ii][jj].setIcon(images[0]);
								gameLost.set(true); // sets it lost
								showMessageDialog(null, "You have lost, please start again!");
							}
							
							//create x,y co-ordinates to use for move method.
							coord c1 = new coord();
							c1.coord(ii,jj);
							mC.move(c1);
							
							for (int i2 = 0; i2 < board.length; i2++) {
								for (int j2 = 0; j2 < board[0].length; j2++) {
									
									if (mineBoardSquares[i2][j2].getName() == "clear" &&  mask[i2][j2] == true) {
										mineBoardSquares[i2][j2].setText("" + board[i2][j2]);
									}
								}
							}

						}
						//if flag mode is active, add or remove flags at the given position
						else if (flagSwitch.get() % 2 == 1) {
							if (b.getName() == "clear" && (mode - bombsActive.get() > 0)) {
								jlabel.setText("   " + (mode - bombsActive.incrementAndGet()) + "   ");
								b.setIcon(images[1]);
								b.setName("filled");
							}
							else if (b.getName() == "filled") {
								jlabel.setText("   " + (mode - bombsActive.decrementAndGet()) + "   ");
								b.setIcon(null);
								b.setName("clear");
							}
						}

						//check mask after every move, to see if player has won.
						int win = 0;
						for (boolean[] i3 : mask){
							for (boolean j3 : i3) {
								if (j3) win++;
								
							}
						}
						if (win == (144 - mode)) showMessageDialog(null, "Congratulations");
						

					
					//b.setText("1");
					}
				});

				b.setMargin(buttonMargin);
				b.setBackground(Color.WHITE);
				//add to mineBoard.
				mineBoardSquares[i][j] = b;
				mineBoard.add(mineBoardSquares[i][j]);

			}
		}

		frame.getContentPane().add(BorderLayout.NORTH, menubar);
		frame.setVisible(true);
	}

}