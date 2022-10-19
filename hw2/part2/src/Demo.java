public class Demo {

	public static void main(String[] args) {
		EmailAddressBook a = new EmailAddressBook();

		a.add(new PersonalEmail("aaa@gtu.tr", "aaa"));

		a.add(new GroupEmail("g1@gtu.tr", "Group 1"));

		a.add(new PersonalEmail("ccc@gtu.tr", "ccc"), "Group 1");

		a.add(new GroupEmail("g2@gtu.tr", "Group 2"), "Group 1");

		a.add(new GroupEmail("g3@gtu.tr", "Group 3"), "Group 2");
		a.add(new PersonalEmail("fff@gtu.tr", "fff"), "Group 3");

		a.add(new PersonalEmail("eee@gtu.tr", "eee"), "Group 2");

		a.add(new PersonalEmail("ddd@gtu.tr", "ddd"), "Group 1");
		a.add(new PersonalEmail("bbb@gtu.tr", "bbb"));

		a.print();

	}
}
