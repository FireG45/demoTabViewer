package org.herac.tuxguitar.graphics.control;


public class TGTrackSpacing extends TGSpacing {
	
	/***     POSITIONS ARRAY INDICES     ***/
	public static final int POSITION_TOP = 0;
	public static final int POSITION_LOOP_MARKER = 1;
	public static final int POSITION_MARKER = 2;
	public static final int POSITION_TEXT = 3;
	public static final int POSITION_BUFFER_SEPARATOR = 4;
	public static final int POSITION_REPEAT_ENDING = 5;
	public static final int POSITION_CHORD = 6;
	public static final int POSITION_SCORE_UP_LINES = 7;
	public static final int POSITION_SCORE_MIDDLE_LINES = 8;
	public static final int POSITION_SCORE_DOWN_LINES = 9;
	public static final int POSITION_DIVISION_TYPE_1 = 10;
	public static final int POSITION_DIVISION_TYPE_2 = 11;
	public static final int POSITION_EFFECTS = 12;
	public static final int POSITION_TABLATURE_TOP_SEPARATOR = 13;
	public static final int POSITION_TABLATURE = 14;
	public static final int POSITION_LYRIC = 15;
	public static final int POSITION_BOTTOM = 16;
	
	private static final int[][] POSITIONS = new int[][]{
		/** SCORE **/
		new int[]{
				POSITION_TOP,
				POSITION_LOOP_MARKER,
				POSITION_MARKER,
				POSITION_TEXT,
				POSITION_BUFFER_SEPARATOR,
				POSITION_REPEAT_ENDING,
				POSITION_CHORD,
                POSITION_SCORE_DOWN_LINES,
				POSITION_DIVISION_TYPE_1,
				POSITION_DIVISION_TYPE_2,
				POSITION_SCORE_MIDDLE_LINES,
				POSITION_EFFECTS,
				POSITION_SCORE_UP_LINES,
				POSITION_TABLATURE_TOP_SEPARATOR,
				POSITION_TABLATURE,
				POSITION_LYRIC,
				POSITION_BOTTOM
			},
			
		/** TABLATURE **/
		new int[]{
				POSITION_TOP,
				POSITION_LOOP_MARKER,
				POSITION_MARKER,
				POSITION_TEXT,
				POSITION_BUFFER_SEPARATOR,
				POSITION_REPEAT_ENDING,
				POSITION_CHORD,
				POSITION_TABLATURE,
				POSITION_LYRIC,
				POSITION_BOTTOM,
				POSITION_SCORE_MIDDLE_LINES,
				POSITION_DIVISION_TYPE_2,
				POSITION_SCORE_UP_LINES,
				POSITION_SCORE_DOWN_LINES,
				POSITION_DIVISION_TYPE_1,
				POSITION_EFFECTS,
				POSITION_TABLATURE_TOP_SEPARATOR
			},
			
		/** SCORE | TABLATURE **/
		new int[]{
				POSITION_TOP,
				POSITION_LOOP_MARKER,
				POSITION_MARKER,
				POSITION_TEXT,
				POSITION_BUFFER_SEPARATOR,
				POSITION_REPEAT_ENDING,
				POSITION_CHORD,
				POSITION_SCORE_MIDDLE_LINES,
				POSITION_SCORE_DOWN_LINES,
				POSITION_DIVISION_TYPE_1,
				POSITION_SCORE_UP_LINES,
				POSITION_DIVISION_TYPE_2,
				POSITION_EFFECTS,
				POSITION_TABLATURE_TOP_SEPARATOR,
				POSITION_TABLATURE,
				POSITION_LYRIC,
				POSITION_BOTTOM
			},
	};
	
	public TGTrackSpacing(TGLayout layout){
		super( layout , POSITIONS , 16);
	}
}