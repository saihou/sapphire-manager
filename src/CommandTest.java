//@author A0097812X

import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.Test;

/*
 * Description: Integration tests for each of the Concrete Commands (6 in total)
 */

public class CommandTest {
	
	private static final String ERROR_NO_RECORDS_FOUND = "ERROR: No records found!";
	private static final String ERROR_ENTERING_A_TIMESTAMP_WO_A_DATE = "ERROR: Entering a timestamp without a date doesn't make sense!";
	private static final String ERROR_LOCATION_MUST_START_WITH_A_LETTER = "ERROR: Location must start with a letter.";
	private static final String ERROR_INVALID_KEYWORDS = "ERROR: Invalid keyword(s)!";
	private static final String ERROR_NOTHING_TO_REMOVE = "ERROR: Nothing to remove!";
	private static final String ERROR_CANNOT_REMOVE_DATE_WITHOUT_REMOVING_TIME = "ERROR: Cannot remove date without removing time!";
	private static final String ERROR_CANNOT_REMOVE_SOMETHING_NOT_PRESENT = "ERROR: Cannot remove something not present!";
	private static final String ERROR_COMMAND_FROM_AND_AT_ARE_MUTUALLY_EXCLUSIVE = "ERROR: Command /from and /at are mutually exclusive.";
	private static final String ERROR_INPUT_ENDING_TIME_WITHOUT_STARTING_TIME = "ERROR: Input ending time without starting time.";
	private static final String ERROR_INPUT_STARTING_TIME_WITHOUT_ENDING_TIME = "ERROR: Input starting time without ending time.";
	private static final String ERROR_INPUT_DURATION_IS_NOT_VALID = "ERROR: Input duration is not valid.";
	private static final String ERROR_INPUT_DEADLINE_TIME_IS_NOT_VALID = "ERROR: Input deadline time is not valid.";
	private static final String ERROR_INPUT_DATE_IS_NOT_VALID = "ERROR: Input date is not valid.";
	private static final String ERROR_INPUT_COMMAND_IS_NOT_VALID = "ERROR: Input Command is not valid.";
	private static final String ERROR_COMMAND_KEYWORD_MISSING = "ERROR: Command keyword is missing.";
	private static final String ERROR_EMPTY_INPUT = "ERROR: Empty input!";
	private static final String ERROR_INVALID_COMMAND = "ERROR: Invalid command entered. Please try again.";
	private static final String ERROR_INVALID_TASK_NUMBER = "ERROR: Invalid task number!";
	private static final String ERROR_INVALID_KEYWORDS_ENTERED = "ERROR: Invalid keyword(s) entered!";
	
	private static final String FEEDBACK_CLEAR_ALL_SUCCESS = "Successfully cleared all tasks.";
	private static final String FEEDBACK_CLEAR_DONE_SUCCESS = "Successfully cleared all done tasks.";
	private static final String FEEDBACK_EDIT_SUCCESS = "Successfully made changes to \"%s\".";
	private static final String FEEDBACK_DEL_SUCCESS = "Successfully deleted 2. %s.";
	
	@Test
	public void test() {
		testClearCommand();
		testAddCommand();
		testEditCommand();
		testDeleteCommand();
		testDisplayCommand();
		testSearchCommand();
	}
	
	private void testClearCommand() {
		assertClearAll();
		assertClearDone();
		assertClearInvalid();
	}
	
	private void testAddCommand() {
		assertAddMemo();
		assertAddFullday();
		assertAddDeadline();
		assertAddDuration();
		assertAddInvalid();
	}
	
	private void testEditCommand() {
		assertEditName();
		assertEditLoc();
		assertEditDate();
		assertEditDeadline();
		assertEditDuration();
		assertEditMark();
		assertEditRemoveTime();
		assertEditRemoveLoc();
		assertEditRemoveDate();
		assertEditCombination();
		assertEditInvalid();
	}
	
	private void testDeleteCommand() {
		assertDelete();
		assertDeleteInvalid();
	}
	
	private void testDisplayCommand() {
		initDisplay();
		
		assertDisplay();
		assertDisplayUndone();
		assertDisplayDone();
		assertDisplayAll();
		assertDisplayOverdue();
		assertDisplayMemos();
		assertDisplayToday();
		assertDisplayInvalid();
	}
	
	private void testSearchCommand() {
		assertSearchName();
		assertSearchDate();
		assertSearchNoResults();
	}
	
	private void assertSearchName() {
		Command search = new SearchCommand();
		search.execute("AB");
		Result r = search.getResult();
		assertEquals("Completed Tasks:\n" +
				"   1. ABC\n" +
				"      10-Oct-2010 <Sun>\n" +
				"      At/By 00:55\n", r.getResult());
	}
	
	private void assertSearchDate() {
		Command search = new SearchCommand();
		search.execute("101010");
		Result r = search.getResult();
		assertEquals("Completed Tasks:\n" +
				"   1. ABC\n" +
				"      10-Oct-2010 <Sun>\n" +
				"      At/By 00:55\n", r.getResult());
	}
	
	private void assertSearchNoResults() {
		Command search = new SearchCommand();
		search.execute("1010101010");
		Result r = search.getResult();
		assertEquals("", r.getResult());
		assertEquals(ERROR_NO_RECORDS_FOUND, r.getSystemFeedback());
	}
	
	private void initDisplay() {
		clearAll();
		
		Command add = new AddCommand();
		add.execute("Overdue task /on 010113 /loc somewhere");
		add.execute("Within 1 week /on 190414 /from 1000 to 2000 /loc over the");
		add.execute("Memo /loc rainbow");
		add.execute("ABC /on 101010 /by 0055");
		
		Command edit = new EditCommand();
		edit.execute("4 /mark done");
	}
	
	private void assertDisplay() {
		Command disp = new DisplayCommand();
		disp.execute("    ");
		Result r = disp.getResult();
		assertEquals("Overdue Tasks:\n" +
				"   1. Overdue task\n" +
				"      01-Jan-2013 <Tue>\n" +
				"      Somewhere\n" +
				"Tasks Occurring/Due Within 7 Days:\n" +
				"   2. Within 1 week\n" +
				"      19-Apr-2014 <Sat>\n" +
				"      From 10:00 to 20:00\n" +
				"      Over the\n" +
				"Memos:\n" +
				"   3. Memo\n" +
				"      Rainbow\n", r.getResult());
	}
	
	private void assertDisplayUndone() {
		Command disp = new DisplayCommand();
		disp.execute("undone");
		Result r = disp.getResult();
		assertEquals("Overdue Tasks:\n" +
				"   1. Overdue task\n" +
				"      01-Jan-2013 <Tue>\n" +
				"      Somewhere\n" +
				"Tasks Occurring/Due Within 7 Days:\n" +
				"   2. Within 1 week\n" +
				"      19-Apr-2014 <Sat>\n" +
				"      From 10:00 to 20:00\n" +
				"      Over the\n" +
				"Memos:\n" +
				"   3. Memo\n" +
				"      Rainbow\n", r.getResult());
	}
	
	private void assertDisplayDone() {
		Command disp = new DisplayCommand();
		disp.execute("done");
		Result r = disp.getResult();
		assertEquals("Completed Tasks:\n" +
				"   1. ABC\n" +
				"      10-Oct-2010 <Sun>\n" +
				"      At/By 00:55\n", r.getResult());
	}
	
	private void assertDisplayAll() {
		Command disp = new DisplayCommand();
		disp.execute("all");
		Result r = disp.getResult();
		assertEquals("Overdue Tasks:\n" +
				"   1. Overdue task\n" +
				"      01-Jan-2013 <Tue>\n" +
				"      Somewhere\n" +
				"Tasks Occurring/Due Within 7 Days:\n" +
				"   2. Within 1 week\n" +
				"      19-Apr-2014 <Sat>\n" +
				"      From 10:00 to 20:00\n" +
				"      Over the\n" +
				"Memos:\n" +
				"   3. Memo\n" +
				"      Rainbow\n" + 
				"Completed Tasks:\n" +
				"   4. ABC\n" +
				"      10-Oct-2010 <Sun>\n" +
				"      At/By 00:55\n", r.getResult());
	}
	
	private void assertDisplayOverdue() {
		Command disp = new DisplayCommand();
		disp.execute("overdue");
		Result r = disp.getResult();
		assertEquals("Overdue Tasks:\n" +
				"   1. Overdue task\n" +
				"      01-Jan-2013 <Tue>\n" +
				"      Somewhere\n", r.getResult());
	}
	
	private void assertDisplayMemos() {
		Command disp = new DisplayCommand();
		disp.execute("memos");
		Result r = disp.getResult();
		assertEquals("Memos:\n" +
				"   1. Memo\n" +
				"      Rainbow\n", r.getResult());
	}
	
	private void assertDisplayToday() {
		addATaskToday();
		String displayDate = getFormattedDateToCompare();
		
		Command disp = new DisplayCommand();
		disp.execute("today");
		Result r = disp.getResult();
		assertEquals("Overdue Tasks:\n" +
				"   1. Overdue task\n" +
				"      01-Jan-2013 <Tue>\n" +
				"      Somewhere\n" + 
				"Today's Tasks:\n" +
				"   2. TaskToday\n" +
				"      " + displayDate + "\n", r.getResult());
	}
	
	private void addATaskToday() {
		SimpleDateFormat systemDateFormat = new SimpleDateFormat("ddMMyy");
		Calendar date = Calendar.getInstance();
		String formattedDate = systemDateFormat.format(date.getTime());
		String executeString = "TaskToday /on " + formattedDate;
		
		Command add = new AddCommand();
		add.execute(executeString);
	}

	private String getFormattedDateToCompare() {
		SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MMM-yyyy <EEE>");
		Calendar date = Calendar.getInstance();
		String displayDate = displayDateFormat.format(date.getTime());
		return displayDate;
	}
	
	private void assertDisplayInvalid() {
		Command disp = new DisplayCommand();
		Result r;
		
		//1 correct keyword 1 wrong keyword
		disp.execute("all abc");
		r = disp.getResult();
		assertEquals(ERROR_INVALID_COMMAND, r.getSystemFeedback());
		
		//0 correct keyword
		disp.execute("rubbish");
		r = disp.getResult();
		assertEquals(ERROR_INVALID_COMMAND, r.getSystemFeedback());
		
		//2 correct keyword
		disp.execute("all done");
		r = disp.getResult();
		assertEquals(ERROR_INVALID_COMMAND, r.getSystemFeedback());
		
	}
	
	private void assertDelete() {
		Command del = new DeleteCommand();
		del.execute("2");
		String taskName = del.getCurrentTask().getName();
		Result r = del.getResult();
		assertEquals(formatString(FEEDBACK_DEL_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertDeleteInvalid() {
		Command del = new DeleteCommand();
		Result r;
		
		//empty string e.g. "   "
		del.execute("    ");
		r = del.getResult();
		assertEquals(ERROR_INVALID_TASK_NUMBER, r.getSystemFeedback());
		
		//invalid choice 0
		del.execute("0    ");
		r = del.getResult();
		assertEquals(ERROR_INVALID_TASK_NUMBER, r.getSystemFeedback());
		
		//invalid choice negative
		del.execute("-100 ");
		r = del.getResult();
		assertEquals(ERROR_INVALID_TASK_NUMBER, r.getSystemFeedback());
		
		//invalid choice abc
		del.execute("abc aas");
		r = del.getResult();
		assertEquals(ERROR_INVALID_TASK_NUMBER, r.getSystemFeedback());
	}
	
	private void assertEditName() {
		Command edit = new EditCommand();
		edit.execute("1 a new name");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditLoc() {
		Command edit = new EditCommand();
		edit.execute("1 /loc new location");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditDate() {
		Command edit = new EditCommand();
		edit.execute("1 /on 100114");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditDeadline() {
		Command edit = new EditCommand();
		edit.execute("1 /on 100114 /at 1000");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditDuration() {
		Command edit = new EditCommand();
		edit.execute("1 /on 100114 /from 1000 to 1001");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditMark() {
		Command edit = new EditCommand();
		edit.execute("3 /mark done");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	
		edit.execute("5 /mark undone");
		taskName = edit.getEditedTask().getName();
		r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditRemoveTime() {
		Command edit = new EditCommand();
		edit.execute("4 Fullday2 /rm time");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditRemoveLoc() {
		Command edit = new EditCommand();
		edit.execute("2 /rm loc");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditRemoveDate() {
		Command edit = new EditCommand();
		edit.execute("5 /rm date");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditCombination() {
		Command edit = new EditCommand();
		
		//add loc remove date
		edit.execute("4 /loc new location /rm date");
		String taskName = edit.getEditedTask().getName();
		Result r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
		
		//remove loc add everything else
		edit.execute("5 /on 140414 /from 0000 to 2358 /rm loc");
		taskName = edit.getEditedTask().getName();
		r = edit.getResult();
		assertEquals(formatString(FEEDBACK_EDIT_SUCCESS, taskName), r.getSystemFeedback());
	}
	
	private void assertEditInvalid() {
		Command edit = new EditCommand();
		Result r;
		
		//empty string e.g. "     "
		edit.execute("     ");
		r = edit.getResult();
		assertEquals(ERROR_INVALID_TASK_NUMBER, r.getSystemFeedback());
		
		//got choice but rest is empty string e.g. "   "
		edit.execute("1     ");
		r = edit.getResult();
		assertEquals(ERROR_EMPTY_INPUT, r.getSystemFeedback());
		
		//empty keyword
		edit.execute("1   /  ");
		r = edit.getResult();
		assertEquals(ERROR_COMMAND_KEYWORD_MISSING, r.getSystemFeedback());
		
		//invalid keyword
		edit.execute("1   /abc");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_COMMAND_IS_NOT_VALID, r.getSystemFeedback());
		
		//spam special characters
		edit.execute("1   //////");
		r = edit.getResult();
		assertEquals(ERROR_COMMAND_KEYWORD_MISSING, r.getSystemFeedback());
		
		//invalid task number
		edit.execute("0   ");
		r = edit.getResult();
		assertEquals(ERROR_INVALID_TASK_NUMBER, r.getSystemFeedback());
		
		//invalid date
		edit.execute("1   /on abc");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_DATE_IS_NOT_VALID, r.getSystemFeedback());
		
		//invalid deadline
		edit.execute("1 /on 101015 /at com1");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_DEADLINE_TIME_IS_NOT_VALID, r.getSystemFeedback());
		
		//invalid duration
		edit.execute("1 /on 101015 /from 1000 to 2360");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_DURATION_IS_NOT_VALID, r.getSystemFeedback());
		
		//got start time but no end time
		edit.execute("1 /on 101015 /from 1000");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_STARTING_TIME_WITHOUT_ENDING_TIME, r.getSystemFeedback());
		
		//got end time but no start time
		edit.execute("1 /on 101015 to 1000");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_ENDING_TIME_WITHOUT_STARTING_TIME, r.getSystemFeedback());
		
		//same start and end time
		edit.execute("1 /on 101015 /from 1000 to 1000");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_DURATION_IS_NOT_VALID, r.getSystemFeedback());
		
		//5 digits for time
		edit.execute("1 /on 101015 /from 10000 to 00000");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_DURATION_IS_NOT_VALID, r.getSystemFeedback());
		
		//7 digits for date
		edit.execute("1 /on 1010151");
		r = edit.getResult();
		assertEquals(ERROR_INPUT_DATE_IS_NOT_VALID, r.getSystemFeedback());
		
		//both /from and /at
		edit.execute("1 /from 1010 to 1511 /by 2359");
		r = edit.getResult();
		assertEquals(ERROR_COMMAND_FROM_AND_AT_ARE_MUTUALLY_EXCLUSIVE, r.getSystemFeedback());
	
		//remove date from a memo
		edit.execute("1 /rm date");
		r = edit.getResult();
		assertEquals(ERROR_CANNOT_REMOVE_SOMETHING_NOT_PRESENT, r.getSystemFeedback());
		
		//remove time from a fullday task
		edit.execute("4 /rm time");
		r = edit.getResult();
		assertEquals(ERROR_CANNOT_REMOVE_SOMETHING_NOT_PRESENT, r.getSystemFeedback());
		
		//invalid removal keyword
		edit.execute("4 /rm asde");
		r = edit.getResult();
		assertEquals(ERROR_INVALID_KEYWORDS, r.getSystemFeedback());
		
		//remove date from a deadline/duration task
		edit.execute("3 /rm date");
		r = edit.getResult();
		assertEquals(ERROR_CANNOT_REMOVE_DATE_WITHOUT_REMOVING_TIME, r.getSystemFeedback());
		
		//empty removal keyword
		edit.execute("3 /rm    ");
		r = edit.getResult();
		assertEquals(ERROR_NOTHING_TO_REMOVE, r.getSystemFeedback());
		
		//string 2 keywords together
		edit.execute("4 /loc /rm date");
		r = edit.getResult();
		assertEquals(ERROR_LOCATION_MUST_START_WITH_A_LETTER, r.getSystemFeedback());
		
		//adding /rm first and then other keywords later
		edit.execute("4 /rm time /loc haha");
		r = edit.getResult();
		assertEquals(ERROR_INVALID_KEYWORDS, r.getSystemFeedback());
	}
	
	private void assertAddMemo() {
		Command add = new AddCommand();
		add.execute("memo");
		Result r = add.getResult();
		assertEquals("add memo", "Successfully added \"Memo\".", r.getSystemFeedback());
	}
	
	private void assertAddFullday() {
		Command add = new AddCommand();
		add.execute("fullday /on 040414");
		Result r = add.getResult();
		assertEquals("add fullday", "Successfully added \"Fullday\".", r.getSystemFeedback());
	}
	
	private void assertAddDeadline() {
		Command add = new AddCommand();
		Result r;
		
		//use "at" keyword
		add.execute("deadline /on 110414 /at 1000");
		r = add.getResult();
		assertEquals("add deadline", "Successfully added \"Deadline\".", r.getSystemFeedback());
		
		//use "by" keyword
		add.execute("deadline2 /on 160414 /by 1300");
		r = add.getResult();
		assertEquals("add deadline", "Successfully added \"Deadline2\".", r.getSystemFeedback());
	}
	
	private void assertAddDuration() {
		Command add = new AddCommand();
		add.execute("duration /on 040614 /from 1000 to 1200");
		Result r = add.getResult();
		assertEquals("add duration", "Successfully added \"Duration\".", r.getSystemFeedback());
	}
	
	private void assertAddInvalid() {
		Command add = new AddCommand();
		Result r;
		
		//empty string e.g. "     "
		add.execute("   ");
		r = add.getResult();
		assertEquals("user never enter task name", 
				ERROR_EMPTY_INPUT, r.getSystemFeedback());
		
		//empty keyword
		add.execute("empty keyword /      ");
		r = add.getResult();
		assertEquals("user gives empty keyword", ERROR_COMMAND_KEYWORD_MISSING, r.getSystemFeedback());
		
		//empty task name
		add.execute("/");
		r = add.getResult();
		assertEquals("user gives empty taskName", "ERROR: No task name.", r.getSystemFeedback());
		
		//invalid keyword
		add.execute("invalid keyword /something");
		r = add.getResult();
		assertEquals("user gives invalid keyword", ERROR_INPUT_COMMAND_IS_NOT_VALID, r.getSystemFeedback());
		
		//spam special characters
		add.execute("spam special characters keyword ///////");
		r = add.getResult();
		assertEquals("user gives empty keyword", ERROR_COMMAND_KEYWORD_MISSING, r.getSystemFeedback());

		//deadine without date
		add.execute("time without date /at 0010");
		r = add.getResult();
		assertEquals("user enters deadline without date", 
				ERROR_ENTERING_A_TIMESTAMP_WO_A_DATE, r.getSystemFeedback());
		
		//duration without date
		add.execute("time without date /from 0010 to 0011");
		r = add.getResult();
		assertEquals("user enters duration without date", 
				ERROR_ENTERING_A_TIMESTAMP_WO_A_DATE, r.getSystemFeedback());
	
		//invalid duration
		add.execute("invalid time /on 040414 /from abcd to -7889");
		r = add.getResult();
		assertEquals("user enters invalid duration", 
				ERROR_INPUT_DURATION_IS_NOT_VALID, r.getSystemFeedback());
		
		//invalid deadine
		add.execute("invalid time /on 040414 /at -656");
		r = add.getResult();
		assertEquals("user enters invalid deadline", 
				ERROR_INPUT_DEADLINE_TIME_IS_NOT_VALID, r.getSystemFeedback());
	
		//got start time but no end time
		add.execute("invalid time /on 040414 /from 1009");
		r = add.getResult();
		assertEquals("user enters start time but no end time", 
				ERROR_INPUT_STARTING_TIME_WITHOUT_ENDING_TIME, r.getSystemFeedback());
		
		//got end time but no start time
		add.execute("invalid time /on 040414 to 2359");
		r = add.getResult();
		assertEquals("user enters end time but no start time", 
				ERROR_INPUT_ENDING_TIME_WITHOUT_STARTING_TIME, r.getSystemFeedback());
		
		//same start and end time
		add.execute("invalid time /on 040414 /from 2359 to 2359");
		r = add.getResult();
		assertEquals("user enters same start time and end time", 
				ERROR_INPUT_DURATION_IS_NOT_VALID, r.getSystemFeedback());
		
		//5 digits for time
		add.execute("5 digits time /on 040414 /by 00000");
		r = add.getResult();
		assertEquals("user enters 5 digits for time", 
				ERROR_INPUT_DEADLINE_TIME_IS_NOT_VALID, r.getSystemFeedback());
		
		//7 digits for date
		add.execute("7 digits date /on 0104151 /by 0000");
		r = add.getResult();
		assertEquals("user enters 7 digits for date", 
				ERROR_INPUT_DATE_IS_NOT_VALID, r.getSystemFeedback());
		
		//extra spaces between date
		add.execute("space btw date /on 10 1 1 1 1 /by 0000");
		r = add.getResult();
		assertEquals("user enters spaces between date", 
				ERROR_INPUT_DATE_IS_NOT_VALID, r.getSystemFeedback());
		
		//extra spaces between time
		add.execute("space btw time /on 101111 /by 0 0 0 0");
		r = add.getResult();
		assertEquals("user enters spaces between time", 
				ERROR_INPUT_DEADLINE_TIME_IS_NOT_VALID, r.getSystemFeedback());
		
		//both /from and /at
		add.execute("both from and at /on 101111 /from 1000 to 1100 /at 0000");
		r = add.getResult();
		assertEquals("user enters spaces between time", 
				ERROR_COMMAND_FROM_AND_AT_ARE_MUTUALLY_EXCLUSIVE, r.getSystemFeedback());
		
		//string 2 keywords together
		add.execute("2 keywords together /loc /rm date");
		r = add.getResult();
		assertEquals(ERROR_LOCATION_MUST_START_WITH_A_LETTER, r.getSystemFeedback());
	}
	
	private void assertClearAll() {
		clearAll();
	}

	private void clearAll() {
		Command clear = new ClearCommand();
		clear.execute("all");
		Result r = clear.getResult();
		assertEquals(FEEDBACK_CLEAR_ALL_SUCCESS, r.getSystemFeedback());
	}
	
	private void assertClearDone() {
		Command clear = new ClearCommand();
		Result r;
		
		//clear done by specifying done explicitly
		clear.execute("done");
		r = clear.getResult();
		assertEquals("clear done", 
				FEEDBACK_CLEAR_DONE_SUCCESS, 
				r.getSystemFeedback());
		
		//clear done by giving empty string
		clear = new ClearCommand();
		clear.execute("   ");
		r = clear.getResult();
		assertEquals("clear    ", 
				FEEDBACK_CLEAR_DONE_SUCCESS, 
				r.getSystemFeedback());
	}
	
	private void assertClearInvalid() {
		Command clear = new ClearCommand();
		clear.execute("invalid stuff here");
		Result r = clear.getResult();
		assertEquals("user writes rubbish", 
				ERROR_INVALID_KEYWORDS_ENTERED, 
				r.getSystemFeedback());
	}
	
	private String formatString(String message, String arg) {
		return String.format(message, arg);
	}
}
