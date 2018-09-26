package apps;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HHApplication {

	public static void main(String[] args) throws Exception {
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		int CID = -1, resCounter = 0;
		int dateResMade = -1, hotelRev, rCID, invoice_no, totalPrice;
		String hotelResults, option, ccInfo = "'", ccNumber, ccCheck, hotelsToReview;
		ArrayList<String[]> reservation = new ArrayList<String[]>();
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C://sqlite//HultonHotel_2.db");
			Statement statement = connection.createStatement();
			Scanner input = new Scanner(System.in);
			ResultSet res = null;

			while (true) {
				System.out.println("Welcome to Hulton Hotel! Please select an option:");
				System.out.println();
				System.out.println("Press 1 to register as a new customer");
				System.out.println();
				System.out.println("Press 2 to make a reservation");
				System.out.println();
				System.out.println("Press 3 to write a review");
				System.out.println();
				System.out.println("Press 4 to view administrative statistics");
				System.out.println();
				System.out.println("Press 0 to exit ");
				option = input.next();
				if (option.equals("0")) {
					//totalPrice = Billing(connection, input, 10);
					System.out.println("Have a nice day, goodbye!");
					break;
				} else if (option.equals("1")) {
					newCustomer(connection, input);
					// System.out.println(res);
				}

				else if (option.equals("2")) {
					System.out.println("Please enter your customer ID (CID) to make a reservation:");
					CID = input.nextInt();
					res = statement.executeQuery("SELECT * FROM CUSTOMER WHERE CID =" + CID + ";");
					if (!res.isBeforeFirst()) {
						System.out.println(
								"CID not found, please register as a new user from the main menu before making a reservation.");
						System.out.println();
						System.out.println();
						continue;
					} else {
						while (true) {
							System.out.println("Welcome, " + res.getString(2) + "!");
							hotelResults = viewHotels(connection, input);
							if (hotelResults.equals("resultsFound")) {
								System.out.println("Please select a Hotel # from above to make a reservation");
								String hotelID = input.nextLine();
								makeReservation(connection, hotelID, input, reservation, res);
							}
							System.out.print("Would you like to make another room reservation? Y/y/N/n");
							option = input.nextLine();
							if (option.equalsIgnoreCase("n")) {
								System.out.println("Proceeding to checkout..");
								System.out.println("Proceeding to checkout, please enter credit card number:");
								ccNumber = input.nextLine();
								ccCheck = checkCC(connection, ccNumber, input);
								ccInfo = ccInfo(input, ccInfo, ccNumber, ccCheck);
								invoice_no = updateDatabase(connection, reservation, ccInfo, ccNumber, CID, ccCheck);
								System.out.println("Tables updated. InvoiceNo:" + invoice_no);
								totalPrice = Billing(connection, input, invoice_no);
								updateTotalSpent(connection, totalPrice, invoice_no, CID);
								Billing(connection, input, invoice_no);
								reservation.clear();
								break;
							}
						}
					}

				} else if (option.equals("3")) {
					String repeatRev;
					System.out.println("Welcome to the review page! Please enter your CID:");
					rCID = input.nextInt();
					res = statement.executeQuery("SELECT * FROM CUSTOMER WHERE CID =" + rCID + ";");
					if (!res.isBeforeFirst()) {
						System.out.println(
								"CID not found, please register as a new user from the main menu and make a reservation before writing a review.");
						System.out.println();
						continue;
					} else {
						do {
							System.out.println("Welcome, " + res.getString(2) + "!");
							hotelsToReview = hotelsToReview(connection, rCID);
							if (hotelsToReview.equals("n")) {
								System.out.println("Oops! You have to make a reservation before writing a review.");
								break;
							} else {
								System.out.println("Enter the hotel number which you would like to review:");
								hotelRev = input.nextInt();
								reviewHotel(input, connection, hotelRev, rCID);
							}
							System.out.println("Would you like to review another hotel? (Y/y/N/n)");
							repeatRev = input.nextLine();
							if (repeatRev.equalsIgnoreCase("n")) {
								break;
							}
						} while (true);

					}

				} else if (option.equals("4")) {
					int statsOption;
					String begTime, endTime;
					do {
						System.out.println("Welcome to the administratie statistics page. Please select an option:");
						System.out.println();
						System.out.println(
								"Press 1 to to view highest rated room type for a particular time period for each hotel");
						System.out.println();
						System.out.println(
								"Press 2 to view the 5 best customers based on money spent for a particular time period");
						System.out.println();
						System.out.println("Press 3 to view highest rated breakfast for a particular time period");
						System.out.println();
						System.out.println("Press 4 to view highest rated service for a particular time period");
						System.out.println("Press 0 to return back to the main menu");
						statsOption = input.nextInt();
						input.nextLine();
						System.out.println("Please enter start date (mm/dd/yyyy):");
						begTime = input.nextLine();
						System.out.println("Please enter end date (mm/dd/yyyy):");
						endTime = input.nextLine();

						if (statsOption == 1) {
							statistics(connection, begTime, endTime, "1");
						} else if (statsOption == 2) {
							statistics(connection, begTime, endTime, "2");
						} else if (statsOption == 3) {
							statistics(connection, begTime, endTime, "3");
						} else if (statsOption == 4) {
							statistics(connection, begTime, endTime, "4");
						} else if (statsOption == 0) {
							break;
						}
					} while (true);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) { // Use SQLException class instead.
				System.err.println(e);
			}
		}
	}

	public static void fiveBestCustomers(Connection conn, String begTime, String endTime) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null;

	}
	
	
	
	
	public static void statistics_2(Connection conn, String begTime, String endTime, String statsFlag) throws SQLException{
		
		Statement statement2 = conn.createStatement();
		ResultSet res = null,res2 = null, res3 = null, res4 = null;
		ArrayList<String> RIDsOrCIDs = new ArrayList<String>();
		String checkInDate, needRID, RIDchain = "RID=", rDate, CIDchain = "CID=", relevantTable = "";
		int lendate = 0;
		for(int i = 0 ; i<lendate ;i++){
			
			
		}
		
		//return();
		
	}
	
	
	
	
	

	public static void statistics(Connection conn, String begTime, String endTime, String statsFlag)
			throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null,res2 = null, res3 = null, res4 = null;
		ArrayList<String> RIDsOrCIDs = new ArrayList<String>();
		String checkInDate, needRID, RIDchain = "RID=", rDate, CIDchain = "CID=", relevantTable = "";
		boolean isInRange = false;
		if (statsFlag.equals("1")) {
			relevantTable = "ROOM_REVIEW";
		} else if (statsFlag.equals("3")) {
			relevantTable = "BREAKFAST_REVIEW";
		} else if (statsFlag.equals("4")) {
			relevantTable = "SERVICE_REVIEW";
		}
		if (statsFlag.equals("1") || statsFlag.equals("3") || statsFlag.equals("4")) {
			// bs checkout date for check room availibility method is going to
			// be 12122017
			// checkroomavailibility returning true means this review falls
			// within our desired range, so we need the RID
		res = statement.executeQuery("SELECT RID,CheckInDate FROM " + relevantTable + ";");
			while (res.next()) {
				checkInDate = res.getString(2);
				isInRange = checkRoomAvailability(checkInDate, "12122017", begTime, endTime, "stats");
				if (isInRange) {
					RIDsOrCIDs.add(res.getString(1));
				}
			}
			// getting all RIDs that I need huehue
			for (int i = 0; i < RIDsOrCIDs.size(); i++) {
				RIDchain += RIDsOrCIDs.get(i) + " OR RID=";
			}
			// strip trailing end
			RIDchain = RIDchain.substring(0, RIDchain.length() - 4);
		}
		if (statsFlag.equals("1")) {
			res = statement.executeQuery(
					"SELECT HotelID,Rtype,MAX(AVERAGE_RATING) AS HIGHEST_RATING FROM (SELECT RID,HotelID,Rtype,AVG(Rating) AS AVERAGE_RATING"
							+ " FROM ROOM_REVIEW NATURAL JOIN ROOM GROUP BY HotelID, Rtype) WHERE " + RIDchain
							+ " GROUP BY RTYPE;");

			System.out.println("The highest rated room types for each hotel between " + dateIntString(begTime) + " and "
					+ dateIntString(endTime) + " is below:");
			System.out.println("HotelID------Rtype------HighestRating");
			while (res.next()) {
				System.out.println(res.getString(1) + "  " + res.getString(2) + "  " + res.getString(3));
			}
		} else if (statsFlag.equals("3")) {
			res3 = statement.executeQuery(
					"SELECT Btype,MAX(AVERAGE_RATING) AS HIGHEST_RATING FROM (SELECT RID,Btype,AVG(Rating) AS AVERAGE_RATING"
							+ "FROM BREAKFAST_REVIEW GROUP BY BTYPE" + "WHERE " + RIDchain + ";");
			System.out.println("The highest rated breakfast type across all hotels between " + dateIntString(begTime) + " and "
					+ dateIntString(endTime) + " is below:");
			System.out.println("Breakfast------HighestRating");
			while (res3.next()) {
				System.out.println(res3.getString(1) + "  " + res3.getString(2));
			}
		}
		else if (statsFlag.equals("4")){
			res4 = statement.executeQuery("SELECT Stype,MAX(AVERAGE_RATING) AS HIGHEST_RATING FROM"
					+ "(SELECT RID,Stype,AVG(RATING) AS AVERAGE_RATING FROM SERVICE_REVIEW GROUP BY STYPE) WHERE " + RIDchain + ";");
			
			System.out.println("The highest rated service type across all hotels between " + dateIntString(begTime) + " and "
					+ dateIntString(endTime) + " is below:");
			System.out.println("Service------HighestRating");
			while (res4.next()) {
				System.out.println(res4.getString(1) + "  " + res4.getString(2));
			}
		}
		else if (statsFlag.equals("2")) {
			res2 = statement.executeQuery("SELECT CID,Rdate FROM TOTAL_SPENT");
			// Rdate will be "checkInDate", bs date will be 12122017
			while (res2.next()) {
				rDate = res.getString(2);
				isInRange = checkRoomAvailability(rDate, "12122017", begTime, endTime, "stats");
				if (isInRange) {
					RIDsOrCIDs.add(res2.getString(1));
				}
			}
			// getting all CIDs that fit within the range
			for (int i = 0; i < RIDsOrCIDs.size(); i++) {
				CIDchain += RIDsOrCIDs.get(i) + " OR CID=";
			}
			// strip trailing endd
			CIDchain = CIDchain.substring(0, CIDchain.length() - 4);
			res2 = statement.executeQuery("SELECT CID,Name,MoneySpent FROM TOTAL_SPENT WHERE " + CIDchain
					+ " ORDER BY MoneySpent DESC LIMIT 5;");
			System.out.println("The 5 best customers between " + dateIntString(begTime) + " and "
					+ dateIntString(endTime) + " are below:");
			System.out.println("CID------Name------Money Spent");
			while (res2.next()) {
				System.out.println(res2.getString(1) + "  " + res2.getString(1) + "  " + res2.getString(3));
			}
		}
		RIDsOrCIDs.clear();
		res.close();
		res2.close();
		res3.close();
		res4.close();
		statement.close();
	}

	private static void updateTotalSpent(Connection conn, int totalPrice, int invoice_no, int CID) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res;
		String name = "";
		int rDate = -1;
		// get Name of this customer
		res = statement.executeQuery("SELECT Name FROM CUSTOMER WHERE CID=" + CID + ";");
		while (res.next()) {
			name = res.getString(1);
			System.out.println("Name in total spent:" + name);
		}
		// get RDate
		res = statement.executeQuery("SELECT Rdate FROM RESERVATION WHERE InvoiceNo=" + invoice_no + ";");
		while (res.next()) {
			rDate = Integer.parseInt(res.getString(1));
			System.out.println("Rdate in total spent:" + rDate);
		}

		statement.executeUpdate("INSERT INTO TOTAL_SPENT VALUES(" + CID + ",'" + name + "'," + invoice_no + ","
				+ totalPrice + "," + rDate + ");");
		System.out.println("Total Spent table updated.");
		res.close();
		statement.close();
		return;
	}

	
public static int Billing(Connection conn, Scanner input,int invoice_no) throws Exception {
		
		int Payable_amount_cent = 0;
		float Total [] = new float [20];
        int  HottelId = 0;
		int RoomNo = 0;
		int length_Stay =  0;
		int chi = 0;
		int cho = 0;
		int Disc_Percent = 0 ;
		//int Room_Reservation_data[][]= new int[5][];
		int No_Reservatiosns = 0;
		float Payable_amount = 0;
		
		ResultSet res7 = null;
		Statement statement7 = conn.createStatement();
		res7 = statement7.executeQuery("SELECT * FROM ROOM_RESERVATION R WHERE R.InvoiceNo =" + invoice_no + ";");
		
		/*if (!res7.next() ) {
		    System.out.println("No_Room_Has_Been_Booked_Yet!");
		    Payable_amount_cent = 0;
		    } 
	  else {

		*/
	    ResultSetMetaData rsMetaData = res7.getMetaData();
	    int numberOfColumns = rsMetaData.getColumnCount();
	    for (int i = 1; i <= numberOfColumns ; i++) {
	      String columnName = rsMetaData.getColumnName(i);
	      System.out.print(columnName + "   ");
	    }
	    System.out.println();
	    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
	    System.out.println();
	    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
	    int totalRoomsPrice = 0;
	    int i =0;
	    while (res7.next()) {
	    	//System.out.print(res.getString(1) + " HHH  ");
	    	
	    	System.out.print("invoice_no:" + invoice_no );
	        System.out.print( "	HottelId:" + res7.getString(2) );
	         HottelId = Integer.parseInt(res7.getString(2)); 
	         
	        System.out.print( "	RoomNo:" + res7.getString(3));
	        RoomNo = Integer.parseInt(res7.getString(3));
	        
	       //(y + "-" + m + "-" + d);
	        chi = Integer.parseInt(res7.getString(4));
	        String chi1 = Formated_date(res7.getString(4));
	        LocalDate Chi = LocalDate.parse(chi1);
	        System.out.print("   Check In Date:" + chi1 );
	        
	        cho = Integer.parseInt(res7.getString(5));
	        String cho1 = Formated_date(res7.getString(5));
	        System.out.print("   Check Out Date:" + cho1 );
	        LocalDate Cho = LocalDate.parse (cho1);
	        
	        
	        length_Stay = (int)(ChronoUnit.DAYS.between(Chi,Cho));
	        System.out.print("   Lenth of Stay:" + length_Stay + " Days");
	       
	      //System.out.println();  
		  		  
	        
		  int Dicount_Norm_No_days[] = new int [10];
		  int No_Dic_days =0;
		  int No_Norm_days =0 ;
		  
		  int Room_Price = Room_Price(conn, input, HottelId, RoomNo );
		  float rpshow = ((float) Room_Price )/100;
		  totalRoomsPrice += Room_Price;
		  
		  int BF_Price = Breakfast_Price(conn, input, HottelId, RoomNo, chi );
		  float bpshow = ((float) BF_Price )/100;

		  
		  int S_Price = Service_Price(conn, input, HottelId, RoomNo, chi );
		  float spshow = ((float) S_Price )/100;

		  
		  Dicount_Norm_No_days = Room_Disc_Norm_No_of_days(conn, input, HottelId, RoomNo, chi,cho, length_Stay);
		  
		  No_Dic_days = Dicount_Norm_No_days[0];
		  if(No_Dic_days == 0){
			  No_Norm_days = length_Stay;  
		  }
		  else{
		  No_Norm_days = Dicount_Norm_No_days[1];
		  }
		  Disc_Percent = Dicount_Norm_No_days[2];
		    
		  Total[i] = ((No_Dic_days * (Room_Price * (100 - Disc_Percent)/100) )) + (No_Norm_days * (Room_Price + BF_Price + S_Price));
		  float tpshow = ((float) Total[i] )/100;
		  
		  
		  System.out.println("		Room_Price_Per_Day: $" +  rpshow);
		  System.out.print("		Total_Breakfasts_Price: $" +  bpshow);
		  System.out.print("		Total_Services_Price: $" + spshow);
		  System.out.print("\n"+"		Total payable Amount for this Room: $" + tpshow +  "\n");
		  System.out.println("\n"+"*****************************************************************************************************************************************");

		  		  
		  i++;
	    }
	    
	    //System.out.println("\n" + "Total_Room_Price: " +  totalRoomsPrice + "$" + "\n");
	    
	    No_Reservatiosns = i;
	    
	    for(int k = 0; k< No_Reservatiosns ; k++)
	    {
	    	Payable_amount += Total[k]; 
	    }
	    float pashow = Payable_amount/100;
	    System.out.println("\n" + "------------Payable Amount------------" +"\n"+ "		$"+ pashow + "\n");
	    
	    Payable_amount_cent = (int)(Payable_amount);
		
		
		res7.close();
		statement7.close();
		
	 // }
	    return (Payable_amount_cent);
	}	
	
	
	//*************************************************************Room_Disc_Norm_No_of_days************************************************************	
	
	public static int[] Room_Disc_Norm_No_of_days(Connection conn, Scanner input,int HottelId,int RoomNo, int chi, int cho, int length_Stay) throws Exception {
		
		int No_dic_norm_days[] = new int [20];
		
		
		  System.out.println("\n"+"-------------------------------------------------------------------------------------------------------------------------------------");

		  Statement statement = conn.createStatement();
		  ResultSet res6 = null;
		  res6 = statement.executeQuery("SELECT D.Discount,D.StartDate,D.EndDate FROM DISCOUNTED_ROOM D WHERE D.HotelId =" + HottelId + "  "
		  		+ "AND "+"  D.RoomNo = " + RoomNo + ";");
		
		  ResultSetMetaData rsMetaData = res6.getMetaData();
		  int numberOfColumns = rsMetaData.getColumnCount();
		  for (int i = 1; i < numberOfColumns + 1; i++) {
		      String columnName = rsMetaData.getColumnName(i);
		      //System.out.print(columnName + "   ");
		    }
		    
		  int Disc = 0;
		  int SDate = 0;
		  int EDate = 0;
		  
		  /*if (!res6.next() ) {
			    System.out.println("No_Discpount _for_this_Room!");
			    No_dic_norm_days[0] = 0;
		        No_dic_norm_days[1] = 0;
		        No_dic_norm_days[2] = 0;
			    //Payable_amount_cent = 0;
			    
			    } 
		  else {*/
		    while (res6.next()) {
		    	
		         Disc = Integer.parseInt(res6.getString(1));
		        
		         SDate = Integer.parseInt(res6.getString(2));
		        //System.out.print( "  Start Date " + SDate );
				
				 EDate = Integer.parseInt(res6.getString(3));
				//System.out.print( "  End Date " + EDate);
		    }
		
		  String CheckIn_d = Formated_date(String.valueOf( chi ) );
		  String CheckOut_d = Formated_date(String.valueOf( cho) );
		  
		  int D_SD = 0; 
		  int D_ED = 0;
		  int Disc_Percent = Disc ;
		  //D_SD = Dicount_info[0][1];
		  //D_ED = Dicount_info[0][2];
		  D_SD =SDate;
		  D_ED = EDate; 
		  
		  
		  String D_SDate = Formated_date(String.valueOf( D_SD) );
		  String D_EDate = Formated_date(String.valueOf( D_ED) );
		  LocalDate ld_DS = LocalDate.parse(D_SDate);
		  LocalDate ld_DE = LocalDate.parse(D_EDate);
		  LocalDate ld_CI = LocalDate.parse(CheckIn_d);
		  LocalDate ld_CO = LocalDate.parse(CheckOut_d);
		  
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	        
	        Date Dic_startDate = sdf.parse(D_SDate);
	        Date Dic_endDate = sdf.parse(D_EDate);
	        Date chi_Date = sdf.parse(CheckIn_d);
	        Date cho_Date = sdf.parse(CheckOut_d);
	        
	        int No_Dic_days = 0;
	        int No_Norm_days = 0;
	        
	        //if((D_SDate <= chi_Date) && (D_EDate => cho_Date)){}

	        //if((Dic_startDate.after(chi_Date)) && (Dic_startDate.before(cho_Date))||((Dic_endDate.after(chi_Date)) && (Dic_endDate.before(cho_Date)))||(Dic_startDate.before(chi_Date) && //(Dic_endDate.after(cho_Date))) || (Dic_startDate.equals(chi_Date) && (Dic_endDate.equals(chi_Date))))
	        	if(((chi_Date.after(Dic_startDate)) && (chi_Date.before(Dic_endDate)))||((cho_Date.after(Dic_startDate)) && (cho_Date.before(Dic_endDate)))||(chi_Date.before(Dic_startDate) && (cho_Date.after(Dic_endDate))) || (chi_Date.equals(Dic_startDate) && (cho_Date.equals(Dic_endDate))))
	        {
	        	//if((Dic_endDate.after(cho_Date)) || (Dic_endDate.equals(cho_Date)) )
	        	//{
	        		 No_Dic_days = (int)(ChronoUnit.DAYS.between(ld_CI,ld_CO));
	        		 int No_Staied_days = (int)(ChronoUnit.DAYS.between(ld_CI,ld_CO));
	        		 No_Norm_days = No_Staied_days - No_Dic_days;
	        		 //No_Dic_days = 0;
	        		 //No_Norm_days = (int)(ChronoUnit.DAYS.between(ld_CI,ld_DS)) -1 ;
	        		 System.out.println( "		*********Dicount_Amount: %" + Disc + " Discount_Start_Date: " + Dic_startDate + " Discount_End_Date: " + Dic_endDate);	 
	        		 System.out.println( "		*********Discount_Covers_whole_Duratoin*********");
	        	//}
	        	
	        	//else if((Dic_endDate.before(cho_Date)))
	        	//{
	        		/*
	        		 No_Dic_days = (int)(ChronoUnit.DAYS.between(ld_DS,ld_DE));
	        		 int No_Staied_days = (int)(ChronoUnit.DAYS.between(ld_CI,ld_CO));
	        		 No_Norm_days = No_Staied_days - No_Dic_days;
	        		 System.out.println( "		*********Dicount_Amount: " + Disc + " Discount_Start_Date: " + Dic_startDate + " Discount_End_Date: " + Dic_endDate);	     
	        		 */   		
	        	//}  	
	        }
	        /*
	        else if((Dic_endDate.after(chi_Date)) && (Dic_endDate.before(cho_Date)))
	        		{
	        	
	        		if((Dic_startDate.before(chi_Date)) || (Dic_startDate.equals(chi_Date)) )
	        		{
	        		
	        		 No_Dic_days = (int)(ChronoUnit.DAYS.between(ld_CI,ld_DE));
	        		 No_Norm_days = (int)(ChronoUnit.DAYS.between(ld_DE,ld_CO)) -1;
	        		 System.out.println( "		*********Dicount_Amount: " + Disc + " Discount_Start_Date: " + Dic_startDate + " Discount_End_Date: " + Dic_endDate);	        		
	        		}
	        	
	        		else if((Dic_startDate.after(chi_Date)))
	        		{
	        		
	        		 No_Dic_days = (int)(ChronoUnit.DAYS.between(ld_DS,ld_DE));
	        		 int No_Staied_days = (int)(ChronoUnit.DAYS.between(ld_CI,ld_CO));
	        		 No_Norm_days = No_Staied_days - No_Dic_days;
	        		 System.out.println( "		*********Dicount_Amount: " + Disc + " Discount_Start_Date: " + Dic_startDate + " Discount_End_Date: " + Dic_endDate);	        		
	        		}  	}
	       
	        else if((Dic_startDate.before(chi_Date) && (Dic_endDate.after(cho_Date))) || (Dic_startDate.equals(chi_Date) && (Dic_endDate.equals(chi_Date))) )
	        {
	         No_Dic_days = (int)(ChronoUnit.DAYS.between(ld_CI,ld_CO));
       		 No_Norm_days = 0;
    		 System.out.println( "		*********Dicount_Amount: " + Disc + " Discount_Start_Date: " + Dic_startDate + " Discount_End_Date: " + Dic_endDate);	        		
	        	
	        }*/
	        else
	        {
	        	No_Dic_days = 0;
	        	No_Norm_days = length_Stay;
	        	System.out.println( "		******No Dicount Available At this time of the year******" );
	        	
	        }
	        	
	        
	        No_dic_norm_days[0] = No_Dic_days;
	        No_dic_norm_days[1] = No_Norm_days;
	        No_dic_norm_days[2] = Disc_Percent;
		  //}
	        
	        return (No_dic_norm_days);
		  
		}
	
//**************************************************************Service_Price***********************************************************	
	
public static int Service_Price (Connection conn, Scanner input,int HottelId,int RoomNo, int chi) throws Exception {
	  
	System.out.println("\n"+"-------------------------------------------------------------------------------------------------------------------------------------");

	//System.out.println( "" );
		int SV_Price_Tot = 0;
		  Statement statement1 = conn.createStatement();
		  ResultSet res4 = null;
		  res4 = statement1.executeQuery("SELECT S.Stype FROM RRESV_SERVICE S WHERE S.HotelId =" + HottelId + "  "
		  		+ "AND "+"  S.RoomNo = " + RoomNo + " AND "+"  S.CheckInDate = " + chi + ";");
		  

		  ResultSetMetaData rsMetaData = res4.getMetaData();
		  int numberOfColumns = rsMetaData.getColumnCount();
		  for (int i = 1; i <= numberOfColumns ; i++) {
		      String columnName = rsMetaData.getColumnName(i);
		      //System.out.print(columnName + "   ");
		    }
		  
		    while (res4.next()) {
		    	
		        String S_typ = (res4.getString(1));
		        System.out.print( "\n"+"		Sevrice_Type: " + S_typ + ",	");
		        
		        ResultSet res5 = null;
		        Statement statement2 = conn.createStatement();
				res5 = statement2.executeQuery("SELECT SS.Sprice FROM SERVICE SS WHERE SS.HotelId =" + HottelId + "  "
				  		+ "AND "+"  SS.Stype = '" + S_typ + "' ;");
				
				int S_Price_1 = Integer.parseInt(res5.getString(1));
				float spshow = (float)S_Price_1/100;
				System.out.print( "		Service_Price: $" + spshow + ",	");
				res5.close();
				SV_Price_Tot += (S_Price_1);
		        
		    }
			  
		  return (SV_Price_Tot);	
	}
	
	//**************************************************************Breakfast_Price***********************************************************
	
	public static int Breakfast_Price (Connection conn, Scanner input,int HottelId,int RoomNo, int chi) throws Exception {
		  System.out.println("\n"+"-------------------------------------------------------------------------------------------------------------------------------------");

		//System.out.println( "" );
			//int tot_1[]=new int[50];
			int BF_Price_Tot = 0;
		    
			  ResultSet res2 = null;
			  Statement statement1 = conn.createStatement();
			  res2 = statement1.executeQuery("SELECT R.Btype, R.NoOfOrders FROM RRESV_BREAKFAST R WHERE R.HotelId =" + HottelId + "  "
			  		+ "AND "+"  R.RoomNo = " + RoomNo + " AND "+"  R.CheckInDate = " + chi + ";");  
			    
			 
			  ResultSetMetaData rsMetaData = res2.getMetaData();
			  int numberOfColumns = rsMetaData.getColumnCount();
			  for (int i = 1; i <= numberOfColumns ; i++) {
			      String columnName = rsMetaData.getColumnName(i);
			      //System.out.print(columnName + "   ");
			    }
			    
			  int i= 0;
			  int ivalue= 0;
			    while (res2.next()) {
			    	
			        String BF_typ = (res2.getString(1));
			        System.out.print( "\n" + "		BreakFast_Type: " + BF_typ + ",	");
			        int no_Ord =  Integer.parseInt(res2.getString(2));
			        System.out.print( "  Number_of_Orders: " + no_Ord+ ",	");
			        
			       
			        ResultSet res3 = null;
			        Statement statement2 = conn.createStatement();
					res3 = statement2.executeQuery("SELECT B.Bprice FROM BREAKFAST B WHERE B.HotelId =" + HottelId + "  "
					  		+ "AND "+"  B.Btype = '" + BF_typ + "';");
					int BF_Price_1 = Integer.parseInt(res3.getString(1));
					float bpshow= (float) BF_Price_1/100;
					System.out.print( "  Breakfast_Price: $" + bpshow+ ",	");
					res3.close();
			        
			        BF_Price_Tot += (BF_Price_1 * no_Ord);
			        
			    }
			    
				  
			  return (BF_Price_Tot);	
		}
	  		  
	//**************************************************************Room_Price***********************************************************	

		public static int Room_Price(Connection conn, Scanner input,int HottelId,int RoomNo) throws Exception {

			Statement statement = conn.createStatement();
			
			  ResultSet res2 = null;
			  res2 = statement.executeQuery("SELECT R.Price FROM ROOM R WHERE R.HotelId =" + HottelId + " AND" + " RoomNo = "+ RoomNo +";");
			  int Room_Price = Integer.parseInt(res2.getString(1));
			
			  return (Room_Price);
			
		}
		
		
		/*
		 * public static void checkCC(Connection conn, int CID) throws SQLException{
		 * Statement statement = conn.createStatement(); ResultSet res = null; res =
		 * statement.executeQuery("SELECT * FROM CREDIT_CARD WHERE ") }
		 */

	//*************************************************************Formated_date************************************************************		 
	public static String Formated_date (String date){
	 
		if (date.length()<8){
			date = ("0"+ date);
			//System.out.print(date );
		}
			String m = date.substring(0,2);
			//System.out.print("  m " + m );
			
		//********************************Formated_date(HHApplication.java:452)
			String d =date.substring(2, date.length() - 4);
			//System.out.print("  d " + d );
			String y =date.substring(4);
			//System.out.print("  y " + y );
			String Date = (y + "-" + m + "-" + d);
		return(Date);
	}	
	//**************************************************************checkRoomAvailability***********************************************************


	
	public static void reviewHotel(Scanner input, Connection conn, int hotelRev, int rCID) throws SQLException {
		int option, invoiceNo, rating;
		boolean confirmRes, revsAdded;
		String text, bfRev, servRev, roomRev;
		// res = statement.executeQuery("SELECT InvoiceNo FROM ")
		do {
			System.out.println("Press 1 to review a room from hotel " + hotelRev);
			System.out.println();
			System.out.println("Press 2 to review a breakfast from hotel " + hotelRev);
			System.out.println();
			System.out.println("Press 3 to review a service from hotel " + hotelRev);
			option = input.nextInt();
			input.nextLine();
			if (option == 1) {
				System.out.println();
				System.out.println("Checking your room reservations..");
				confirmRes = checkRoomRes(conn, hotelRev, rCID);
				if (confirmRes) {
					System.out.println("Enter the room number and the check in date from hotel " + hotelRev
							+ " that you'd like to review (eg '1 01/20/2017'):");
					roomRev = input.nextLine();
					System.out.println("Please enter a rating (1-10):");
					rating = input.nextInt();
					input.nextLine();
					System.out.println(
							"Please enter any comments (n/a if you do not have any comments), limit 500 characters:");
					text = input.nextLine().toUpperCase();
					revsAdded = insertRoomReview(conn, hotelRev, roomRev, rCID, rating, text);
					if (revsAdded) {
						System.out.println("Room review submitted, thank you.");
					}
				} else {
					System.out.println("Oops! You have not made room reservations so you cannot write a review!");
				}
			} else if (option == 2) {
				System.out.println("Checking your breakfast reservations..");
				confirmRes = checkBFastRes(conn, hotelRev, rCID);
				if (confirmRes) {
					System.out.println("Enter the breakfast type, room number, and check in date from hotel " + hotelRev
							+ " that you'd like to review (eg 'American 9 01/10/2017':");
					bfRev = input.nextLine().toUpperCase();
					System.out.println("Please enter a rating (1-10):");
					rating = input.nextInt();
					input.nextLine();
					System.out.println(
							"Please enter any comments (n/a if you do not have any comments), limit 500 characters:");
					text = input.nextLine().toUpperCase();
					revsAdded = insertBForServRev(conn, hotelRev, bfRev, rCID, rating, text, "bFastRev");
					if (revsAdded) {
						System.out.println("Breakfast review submitted, thank you.");
					}
				} else {
					System.out.println("Oops! You have not made breakfast reservations in hotel " + hotelRev
							+ " so you cannot write a review!");
				}
			} else if (option == 3) {
				System.out.println("Checking your service reservations..");
				confirmRes = checkServRes(conn, hotelRev, rCID);
				if (confirmRes) {
					System.out.println("Enter the service type, room number, and check in date from hotel " + hotelRev
							+ " that you'd like to review (eg ' 8 01/10/2017):");
					servRev = input.nextLine().toUpperCase();
					System.out.println("Please enter a rating (1-10):");
					rating = input.nextInt();
					input.nextLine();
					System.out.println(
							"Please enter any comments (n/a if you do not have any comments), limit 500 characters:");
					text = input.nextLine().toUpperCase();
					revsAdded = insertBForServRev(conn, hotelRev, servRev, rCID, rating, text, "servRev");
					if (revsAdded) {
						System.out.println("Service review submitted, thank you.");
					}
				} else {
					System.out.println("Oops! You have not made breakfast reservations in hotel " + hotelRev
							+ " so you cannot write a review!");
				}
			}
			System.out.println("Would you like to review anything else from hotel " + hotelRev + " (Y/y/N/n)?");
			if (input.nextLine().equalsIgnoreCase("n")) {
				break;
			}

		} while (true);

	}

	public static boolean insertBForServRev(Connection conn, int hotelRev, String revItem, int rCID, int rating,
			String text, String revFlag) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null;
		String values = "", bfOrServ, roomNo, checkInDate;
		int RID;
		boolean b = false;
		String[] strSplit = revItem.split("\\s+");
		bfOrServ = strSplit[0];
		roomNo = strSplit[1];
		checkInDate = convertDate(strSplit[2]);
		if (revFlag.equals("bFastRev")) {
			res = statement.executeQuery("SELECT MAX(RID) FROM BREAKFAST_REVIEW;");
			RID = Integer.parseInt(res.getString(1));
			RID++;
			values += RID + "," + hotelRev + "," + roomNo + "," + checkInDate + ",'" + bfOrServ + "'," + rCID + ","
					+ rating + ",'" + text + "'";
			statement.executeUpdate("INSERT INTO BREAKFAST_REVIEW VALUES(" + values + ");");
			b = true;
		} else if (revFlag.equals("servRev")) {
			res = statement.executeQuery("SELECT MAX(RID) FROM SERVICE_REVIEW;");
			RID = Integer.parseInt(res.getString(1));
			RID++;
			values += RID + "," + hotelRev + "," + roomNo + "," + checkInDate + ",'" + bfOrServ + "'," + rCID + ","
					+ rating + ",'" + text + "'";
			statement.executeUpdate("INSERT INTO SERVICE_REVIEW VALUES(" + values + ");");
			b = true;
		}
		res.close();
		statement.close();
		return b;
	}

	public static boolean insertRoomReview(Connection conn, int hotelRev, String roomRev, int rCID, int rating,
			String text) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null;
		String values = "", roomNo, checkInDate;
		int RID;
		boolean b = false;
		String[] splitStr = roomRev.split("\\s+");
		roomNo = splitStr[0];
		checkInDate = convertDate(splitStr[1]);
		res = statement.executeQuery("SELECT MAX(RID) FROM ROOM_REVIEW");
		RID = Integer.parseInt(res.getString(1));
		RID++;
		values += RID + "," + hotelRev + "," + roomNo + "," + checkInDate + "," + rCID + "," + rating + ",'" + text
				+ "'";
		statement.executeUpdate("INSERT INTO ROOM_REVIEW VALUES(" + values + ");");
		b = true;

		res.close();
		statement.close();
		return b;
	}

	public static boolean checkServRes(Connection conn, int hotelRev, int rCID) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null;
		boolean b = false;
		int counter = 1;
		String serv, roomNo, checkInDate;
		res = statement.executeQuery(
				"SELECT Stype,RoomNo,CheckInDate from (RESERVATION NATURAL JOIN ROOM_RESERVATION) NATURAL JOIN RRESV_SERVICE WHERE CID="
						+ rCID + " AND HotelID=" + hotelRev + ";");
		if (res.isBeforeFirst()) {
			System.out.println("The following are service types in hotel " + hotelRev
					+ " that you reserved with corresponding room numbers and check in dates.");
			while (res.next()) {
				serv = res.getString(1);
				roomNo = res.getString(2);
				checkInDate = res.getString(3);
				System.out.println("Service Reservation #:" + counter + " " + serv + " " + roomNo + " " + dateIntString(checkInDate));
				b = true;
				counter++;
			}
		} else {
			b = false;
		}
		res.close();
		statement.close();
		return b;
	}

	public static boolean checkBFastRes(Connection conn, int hotelRev, int rCID) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null;
		boolean b = false;
		String bfast, checkInDate, roomNo;
		int counter = 1;
		res = statement.executeQuery(
				"SELECT Btype,RoomNo,CheckInDate from (RESERVATION NATURAL JOIN ROOM_RESERVATION) NATURAL JOIN RRESV_BREAKFAST WHERE CID="
						+ rCID + " AND HotelID=" + hotelRev + ";");
		if (res.isBeforeFirst()) {
			System.out.println("The following are breakfast types in hotel " + hotelRev
					+ " that you reserved with corresponding room numbers and check in dates:");
			while (res.next()) {
				bfast = res.getString(1);
				roomNo = res.getString(2);
				checkInDate = res.getString(3);
				System.out
						.println("Breakfast reservation #" + counter + " " + bfast + " " + roomNo + " " + dateIntString(checkInDate));
				b = true;
				counter++;
			}
		} else {
			b = false;
		}
		res.close();
		statement.close();
		return b;
	}

	public static boolean checkRoomRes(Connection conn, int hotelRev, int CID) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null;
		boolean b = false;
		String rooms = "", checkInDate;
		int counter = 1;
		res = statement.executeQuery("SELECT RoomNo,CheckInDate FROM Reservation R, Room_Reservation R1 WHERE R.CID ="
				+ CID + " AND R1.InvoiceNo = R.InvoiceNo;");
		if (res.isBeforeFirst()) {
			System.out.println("The following are rooms in hotel " + hotelRev
					+ " that you reserved with corresponding check in dates.");
			while (res.next()) {
				for (int i = 1; i < 3; i++)
					rooms = res.getString(1);
				checkInDate = dateIntString(res.getString(2));
				System.out.println("Room reservation #" + counter + ": " + rooms + " " + checkInDate);
				b = true;
				counter++;
			}
		} else {
			b = false;
		}
		res.close();
		statement.close();
		return b;
	}

	public static String dateIntString(String s) {
		String result = "";
		if (s.length() == 7) {
			result += "0" + s.charAt(0) + "/" + s.substring(1, 3) + "/" + s.substring(3);
		} else if (s.length() == 8) {
			result += s.substring(1, 2) + "/" + s.substring(2, 4) + "/" + s.substring(4);
		}
		return result;
	}

	public static String hotelsToReview(Connection conn, int rCID) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null;
		String s = "";
		res = statement.executeQuery(
				"SELECT DISTINCT HotelID from HOTEL WHERE HOTELID IN(SELECT O.HOTELID FROM RESERVATION R,ROOM_RESERVATION O WHERE R.CID="
						+ rCID + " AND O.InvoiceNo = R.InvoiceNo);");
		if (res.isBeforeFirst()) {
			System.out.println("You have made reservations at the following hotels:");
			while (res.next()) {
				System.out.println("Hotel #: ");
				System.out.println(res.getString(1));
			}
			s = "y";
		} else {
			s = "n";
		}
		res.close();
		statement.close();
		return s;
	}

	public static String ccInfo(Scanner input, String ccInfo, String ccNumber, String ccCheck) {
		System.out.println("In ccInfo method. ccCheck = " + ccCheck);
		if (ccCheck.equalsIgnoreCase("n")) {
			ccInfo += ccNumber;
			System.out.println("Please enter credit card type (ex Visa):");
			ccInfo += "','" + input.nextLine().toUpperCase() + "','";
			System.out.println("Please enter billing address:");
			ccInfo += input.nextLine().toUpperCase() + "','";
			System.out.println("Please enter credit card code:");
			ccInfo += input.nextLine() + "','";
			System.out.println("Please enter expiry date (ex MM/YY):");
			ccInfo += input.nextLine() + "','";
			System.out.println("Please enter name on credit card:");
			ccInfo += input.nextLine().toUpperCase() + "');";
		} else {
			ccInfo = ccNumber;
		}
		return ccInfo;
	}

	public static String checkCC(Connection conn, String ccNumber, Scanner input) throws SQLException {
		ResultSet res = null;
		Statement statement = conn.createStatement();
		String s, cc = "";
		res = statement.executeQuery("SELECT * FROM CREDIT_CARD WHERE Cnumber='" + ccNumber + "';");
		while (res.next()) {
			System.out.println("We have the following credit card on file:");
			System.out.print("Credit Card:");
			for (int i = 1; i <= 6; i++) {
				cc += " " + res.getString(i);
			}
			System.out.println(cc);
			System.out.println();
			System.out.println("Would you like to use this credit card for your reservation? (Y/y/N/N)");
			s = input.nextLine();
			if (s.equalsIgnoreCase("Y")) {
				return cc;
			}
		}
		res.close();
		statement.close();
		return "n";
	}

	public static int updateDatabase(Connection conn, ArrayList<String[]> reservation, String ccInfo, String ccNumber,
			int CID, String ccCheck) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet res = null;
		System.out.println("ccInfo:" + ccInfo);
		int invoiceNo;
		int rDate;
		ZoneId zonedId = ZoneId.of("America/Montreal");
		LocalDate today = LocalDate.now(zonedId);
		System.out.println("today : " + today);
		rDate = convDateToInt(today);
		// need to get new invoice number
		res = statement.executeQuery("SELECT MAX(InvoiceNo) FROM RESERVATION");
		invoiceNo = res.getInt(1);
		invoiceNo++;
		System.out.println("ccInfo:" + ccInfo);
		// if cccheck = no, then new credit card info needs to be inserted into
		// DB
		if (ccCheck.equalsIgnoreCase("n")) {
			statement.execute("INSERT INTO CREDIT_CARD VALUES(" + ccInfo);
		}
		// insert reservation
		statement.execute(
				"INSERT INTO RESERVATION VALUES(" + invoiceNo + "," + CID + ",'" + ccNumber + "'," + rDate + ");");
		// insert all the info about every reservation now that we have an
		// invoice number created
		for (String[] array : reservation) {
			String[] roomReservations = array[0].split("&");
			insertTuples(conn, roomReservations, "roomRes", invoiceNo);
			statement.execute("pragma busy_timeout = 3000;");
			String[] bFastReservations = array[1].split("&");
			insertTuples(conn, bFastReservations, "bFastRes", invoiceNo);
			statement.execute("pragma busy_timeout = 3000;");
			String[] servReservations = array[2].split("&");
			insertTuples(conn, servReservations, "servRes", invoiceNo);
			statement.execute("pragma busy_timeout = 3000;");
		}
		res.close();
		statement.close();
		return invoiceNo;
	}

	public static void insertTuples(Connection conn, String[] reservations, String resType, int invoiceNo)
			throws SQLException {
		Statement statement = conn.createStatement();
		String s = "";
		for (int i = 0; i < reservations.length; i++) {
			s = reservations[i];
			if (!s.isEmpty()) {
				if (resType.equals("roomRes")) {
					System.out.println("Room Reservation:" + reservations[i]);
					statement.executeUpdate("INSERT INTO ROOM_RESERVATION VALUES(" + invoiceNo + s);
				} else if (resType.equals("bFastRes")) {
					System.out.println("Breakfast reservation:" + reservations[i]);
					statement.executeUpdate("INSERT INTO RRESV_BREAKFAST VALUES" + s);
				} else if (resType.equals("servRes")) {
					System.out.println("Service reservation:" + reservations[i]);
					statement.executeUpdate("INSERT INTO RRESV_SERVICE VALUES" + s);
				}
				System.out.println("Database updated for" + resType);
			}
		}

		statement.close();
		return;
	}

	public static int convDateToInt(LocalDate today) {
		String s = today.toString(), m, d, y;
		int rDate;
		if (s.charAt(5) == '0') {
			m = "" + s.charAt(6);
		} else {
			m = s.substring(5, 7);
		}
		d = s.substring(8);
		y = s.substring(0, 4);

		s = m + d + y;
		rDate = Integer.parseInt(s);
		return rDate;
	}

	public static void makeReservation(Connection conn, String hotelID, Scanner input, ArrayList<String[]> reservations,
			ResultSet res) throws SQLException {
		String CID, roomChoice, chkInDate, chkOutDate, rChkInDate, rChkOutDate, CIcopy, COcopy, bfastChoice, servChoice,
				repeatRRes, roomResChain = "", bfChain = "", servChain = "", discRoomRes = "";
		boolean roomAvail = false;
		Statement statement = conn.createStatement();
		String roomType = "";
		int roomNumber = -1;
		String[] roomResDetails = new String[4];
		res = statement
				.executeQuery("SELECT DISTINCT Rtype, Description FROM ROOM R WHERE R.HotelID =" + hotelID + ";");
		System.out.println("The following room types are offered at your hotel of choice:");
		while (res.next()) {
			System.out.print("Room Type:");
			for (int i = 1; i <= 2; i++) {
				System.out.print(" " + res.getString(i));
			}
			System.out.println();
		}
		System.out.println("Please enter the room type you would like to reserve (eg Double):");
		roomType = input.nextLine().toUpperCase();
		res = statement.executeQuery("SELECT RoomNo, Rtype, Price, Description, Floor, Capacity"
				+ " FROM ROOM WHERE Rtype ='" + roomType + "' AND HotelID =" + hotelID + ";");

		while (res.next()) {
			System.out.print("Rooms:");
			for (int i = 1; i <= 6; i++) {
				System.out.print(" " + res.getString(i));
			}
			System.out.println();
		}
		System.out.println("Please enter the room number that you would like to reserve:");
		roomNumber = input.nextInt();
		input.nextLine();
		do {
			System.out.println("Please enter your check in date (eg mm/dd/yyyy)");
			chkInDate = input.nextLine();
			CIcopy = chkInDate;
			chkInDate = convertDate(chkInDate);
			System.out.println("Please enter your check out date (eg mm/dd/yyyy)");
			chkOutDate = input.nextLine();
			COcopy = chkOutDate;
			chkOutDate = convertDate(chkOutDate);
			System.out.println();
			System.out.println("Checking availability of the room on your desired dates...");
			System.out.println();
			res = statement.executeQuery(
					"SELECT HotelID, RoomNo, CheckInDate, CheckOutDate FROM ROOM_RESERVATION WHERE HotelID =" + hotelID
							+ " AND RoomNo=" + roomNumber + ";");
			if (!res.isBeforeFirst()) {
				roomAvail = true;
			} else {
				while (res.next()) {
					rChkInDate = res.getString(3);
					rChkOutDate = res.getString(4);
					roomAvail = checkRoomAvailability(chkInDate, chkOutDate, rChkInDate, rChkOutDate, "n");
					if (roomAvail == false) {
						break;
					}
				}
			}
			if (roomAvail) {
				System.out.println("Room is available on desired dates, for " + CIcopy + " through " + COcopy + "!");
				// checking if check in date coincides with a discounted room
				// for that
				discRoomRes = checkDiscRoom(conn, hotelID, roomNumber, chkInDate, chkOutDate);
				System.out.println();
				roomResChain += "," + hotelID + "," + roomNumber + "," + chkInDate + "," + chkOutDate + ");&";
				System.out.println("Would you like to reserve breakfast for your stay? (Y/y/N/n)");
				bfastChoice = input.nextLine();
				if (bfastChoice.equalsIgnoreCase("y")) {
					bfChain += reserveBFast(conn, input, chkInDate, chkOutDate, hotelID, roomNumber, res);
				}
				System.out.println("Would you like to purchase services for your stay? (Y/y/N/n)");
				servChoice = input.nextLine();
				if (servChoice.equalsIgnoreCase("y")) {
					servChain += reserveService(conn, input, chkInDate, chkOutDate, hotelID, roomNumber);
				}
				/*
				 * roomResDetails[0] = roomResChain; roomResDetails[1] =
				 * bfChain; roomResDetails[2] = servChain;
				 * reservations.add(roomResDetails);
				 */
			} else {
				System.out.println("Oops, room is already booked by another customer on your desired dates.");
			}

			System.out.println("Would you like to reserve this room for another check in/check out date? (Y/y/N/n)");
			repeatRRes = input.nextLine();
			if (repeatRRes.equalsIgnoreCase("n")) {
				roomResDetails[0] = roomResChain;
				roomResDetails[1] = bfChain;
				roomResDetails[2] = servChain;
				reservations.add(roomResDetails);
				break;
			}
		} while (true);
		res.close();
		statement.close();
	}

	public static String checkDiscRoom(Connection conn, String hotelID, int roomNumber, String chkInDate,
			String chkOutDate) throws SQLException {
		// retrieve discounted room info if customer's room choice is in that
		// table
		Statement statement = conn.createStatement();
		String discStartDate;
		String discEndDate;
		ResultSet res = null;
		boolean discount = false;
		String s = "", discRate = "";
		res = statement.executeQuery(
				"SELECT * FROM DISCOUNTED_ROOM WHERE HotelID =" + hotelID + " AND RoomNo=" + roomNumber + ";");
		while (res.next()) {
			s += res.getString(1) + " " + res.getString(2) + " " + res.getString(3) + " " + res.getString(4) + " "
					+ res.getString(5);
			discRate = res.getString(3);
			discStartDate = res.getString(4);
			discEndDate = res.getString(5);
			discount = checkRoomAvailability(chkInDate, chkOutDate, discStartDate, discEndDate, "y");
			if (discount) {
				System.out.println("Good news! The room you selected is at a " + res.getString(3)
						+ "% discount during your check in date!");
				System.out.println();
				return discRate;
			}
		}
		res.close();
		statement.close();
		return "";
	}

	public static String reserveService(Connection conn, Scanner input, String chkInDate, String chkOutDate,
			String hotelID, int roomNumber) throws SQLException {
		String servOption, servChain = "", repeatSR;
		Statement statement = conn.createStatement();
		ResultSet res = null;
		do {
			res = statement.executeQuery("SELECT Stype,Sprice FROM SERVICE WHERE HotelID =" + hotelID + ";");
			System.out.println("The following services are offered at your hotel of choice:");
			while (res.next()) {
				System.out.print("Service:");
				for (int i = 1; i <= 2; i++) {
					System.out.print(" " + res.getString(i));
				}
				System.out.println();
			}
			System.out.println("Please enter the service you would like to add to your room reservation: (eg Laundry)");
			servOption = input.nextLine().toUpperCase();
			servChain += "('" + servOption + "'," + hotelID + "," + roomNumber + "," + chkInDate + ");&";
			if (servOption.equalsIgnoreCase("Laundry")) {
				System.out.println("Laundry service reserved for your room reservation.");
			} else if (servOption.equalsIgnoreCase("Parking")) {
				System.out.println("Parking service reserved for your room reservation.");
			} else if (servOption.equalsIgnoreCase("Airport Pickup/Dropoff")) {
				System.out.println("Airport pickup/dropoff service reserved for your room reservation.");
			} else if (servOption.equalsIgnoreCase("Room Service")) {
				System.out.println("Room service reserved for your room reservation.");
			} else if (servOption.equalsIgnoreCase("WiFi")) {
				System.out.println("WiFi service reserved for your room reservation.");
			}
			System.out.println("Would you like to reserve another service for your room reservation? (Y/y/N/n)");
			repeatSR = input.nextLine();
			if (repeatSR.equalsIgnoreCase("N")) {
				break;
			}
		} while (true);

		System.out.println("Services reserved for your room reservation");
		res.close();
		statement.close();
		return servChain;

	}

	public static String reserveBFast(Connection conn, Scanner input, String chkInDate, String chkOutDate,
			String hotelID, int roomNumber, ResultSet res) throws SQLException {
		String bfOption, bfChain = "", repeatSR;
		int bfNo, roomCapacity, totalBF, reservedBF = 0, maxRes;
		Statement statement = conn.createStatement();
		ResultSet roomCapRes = null;
		boolean canResBF;
		roomCapRes = statement.executeQuery(
				"SELECT Capacity FROM ROOM WHERE HotelID =" + hotelID + " AND RoomNo =" + roomNumber + ";");
		roomCapacity = roomCapRes.getInt(1);
		maxRes = roomCapacity;
		do {
			canResBF = false;
			res = statement
					.executeQuery("SELECT Btype,Bprice,Description FROM BREAKFAST WHERE HotelID =" + hotelID + ";");
			System.out.println("The following breakfast options are offered at your hotel of choice:");
			while (res.next()) {
				System.out.print("Breakfast:");
				for (int i = 1; i <= 3; i++) {
					System.out.print(" " + res.getString(i));
				}
				System.out.println();
			}
			System.out.println(
					"Please enter the breakfast you would like to add to your room reservation: (eg American)");
			bfOption = input.nextLine().toUpperCase();
			do {
				System.out.println("Please enter the number of orders you'd like:");
				bfNo = input.nextInt();
				reservedBF += bfNo;
				if (reservedBF > roomCapacity) {
					System.out.println("You may not reserve more breakfasts than your room capacity (" + roomCapacity
							+ ") allows.");
					System.out.println("You can reserve " + maxRes + " more breakfast(s).");
					reservedBF -= bfNo;
				} else {
					canResBF = true;
					break;
				}
			} while (true);
			if (canResBF) {
				maxRes = roomCapacity - reservedBF;
				bfChain += "('" + bfOption + "'," + hotelID + "," + roomNumber + "," + chkInDate + "," + bfNo + ");&";
				if (bfOption.equalsIgnoreCase("Continental")) {
					System.out.println(bfNo + " order(s) of Continental breakfast reserved for your room reservation.");
				} else if (bfOption.equalsIgnoreCase("Italian")) {
					System.out.println(bfNo + " order(s) of Italian breakfast reserved for your room reservation.");
				} else if (bfOption.equalsIgnoreCase("English")) {
					System.out.println(bfNo + " order(s) of English breakfast reserved for your room reservation.");
				} else if (bfOption.equalsIgnoreCase("American")) {
					System.out.println(bfNo + " order(s) of American breakfast reserved for your room reservation.");
				} else if (bfOption.equalsIgnoreCase("French")) {
					System.out.println(bfNo + " order(s) of French reserved for your room reservation.");
				}
			}
			input.nextLine();
			if (reservedBF < roomCapacity) {
				System.out.println(
						"Would you like to reserve another breakfast type for your room reservation? (Y/y/N/n)");
				repeatSR = input.nextLine();
				if (repeatSR.equalsIgnoreCase("N")) {
					break;
				}
			} else {
				break;
			}
		} while (true);
		res.close();
		statement.close();
		return bfChain;
	}

	/*
	 * public static void checkCC(Connection conn, int CID) throws SQLException{
	 * Statement statement = conn.createStatement(); ResultSet res = null; res =
	 * statement.executeQuery("SELECT * FROM CREDIT_CARD WHERE ") }
	 */

	public static boolean checkRoomAvailability(String chkInDate, String chkOutDate, String rChkInDate,
			String rChkOutDate, String discFlag) {
		int MCIM = -1, MCID = -1, MCIY = -1, MCOM = -1, MCOD = -1, MCOY = -1, RCIM = -1, RCID = -1, RCIY = -1,
				RCOM = -1, RCOD = -1, RCOY = -1;
		String s = "", isBefore = "";
		boolean res = true;
		// converting check in date to int
		if (chkInDate.length() == 7) {
			s = s += chkInDate.charAt(0);
			MCIM = Integer.parseInt(s);
			MCID = Integer.parseInt(chkInDate.substring(1, 3));
			MCIY = Integer.parseInt(chkInDate.substring(3));
		} else if (chkInDate.length() == 8) {
			MCIM = Integer.parseInt(chkInDate.substring(0, 2));
			MCID = Integer.parseInt(chkInDate.substring(2, 4));
			MCIY = Integer.parseInt(chkInDate.substring(4));
		}
		s = "";
		// converting reserved check in date to int
		if (rChkInDate.length() == 7) {
			s = s += rChkInDate.charAt(0);
			RCIM = Integer.parseInt(s);
			RCID = Integer.parseInt(rChkInDate.substring(1, 3));
			RCIY = Integer.parseInt(rChkInDate.substring(3));
		} else if (rChkInDate.length() == 8) {
			RCIM = Integer.parseInt(rChkInDate.substring(0, 2));
			RCID = Integer.parseInt(rChkInDate.substring(2, 4));
			RCIY = Integer.parseInt(rChkInDate.substring(4));
		}
		s = "";
		// converting check out date to int
		if (chkOutDate.length() == 7) {
			s = s += chkOutDate.charAt(0);
			MCOM = Integer.parseInt(s);
			MCOD = Integer.parseInt(chkOutDate.substring(1, 3));
			MCOY = Integer.parseInt(chkOutDate.substring(3));
		} else if (chkOutDate.length() == 8) {
			MCOM = Integer.parseInt(chkOutDate.substring(0, 2));
			MCOD = Integer.parseInt(chkOutDate.substring(2, 4));
			MCOY = Integer.parseInt(chkOutDate.substring(4));
		}
		s = "";
		// converting reserved check out date to int
		if (rChkOutDate.length() == 7) {
			s = s += rChkOutDate.charAt(0);
			RCOM = Integer.parseInt(s);
			RCOD = Integer.parseInt(rChkOutDate.substring(1, 3));
			RCOY = Integer.parseInt(rChkOutDate.substring(3));
		} else if (rChkOutDate.length() == 8) {
			RCOM = Integer.parseInt(rChkOutDate.substring(0, 2));
			RCOD = Integer.parseInt(rChkOutDate.substring(2, 4));
			RCOY = Integer.parseInt(rChkOutDate.substring(4));
		}
		// checking my check in date is before reserved check in date
		isBefore = isBefore(MCIM, MCID, MCIY, RCIM, RCID, RCIY);
		// if yes..
		if (discFlag.equals("n")) {
			if (isBefore.equals("isBefore")) {
				// check if my check out date is before reserved check in date
				isBefore = isBefore(MCOM, MCOD, MCOY, RCIM, RCID, RCIY);
				if (isBefore.equals("isBefore")) {
					return true;
				}
				return false;
			}
			// my check in is after or equal to reserved check in
			else {
				if (isBefore.equals("isSame")) {
					return false;
				}
				// check in date is after, so need to confirm check out date
				isBefore = isBefore(MCIM, MCID, MCIY, RCOM, RCOD, RCOY);
				// my check is before reserved check out
				if (isBefore.equals("isBefore")) {
					return false;
				} else if (isBefore.equals("isSame")) {
					return false;
				}
			}
		} else if (discFlag.equals("y")) {
			if (isBefore.equals("isAfter")) {
				// check if my check in date is before the end of the discount
				// period
				isBefore = isBefore(MCIM, MCID, MCIY, RCOM, RCOD, RCOY);
				if (isBefore.equals("isBefore")) {
					return true;
				} else {
					return false;
				}
			} else {
				res = false;
			}
		} else if (discFlag.equals("stats")) {
			if (isBefore.equals("isBefore")) {
				return false;
			}
			// check in day is after the beginning date, now need to check if
			// it's before end date
			isBefore = isBefore(MCIM, MCID, MCIY, RCOM, RCOD, RCOY);
			if (isBefore.equals("isBefore") || isBefore.equals("isSame")) {
				return true;
			} else {
				res = false;
			}

		}
		return res;

	}

	public static String isBefore(int m1, int d1, int y1, int m2, int d2, int y2) {
		if (y1 < y2) {
			return "isBefore";
		}
		// y1 is = to y2
		if (y1 == y2) {
			if (m1 < m2) {
				return "isBefore";
			} else if (m1 > m2) {
				return "isAfter";
			}
			// m1 and m2 equal
			else if (d1 < d2) {
				return "isBefore";
			} else if (d1 == d2) {
				return "isSame";
			}
		}
		return "isAfter";
	}

	public static void newCustomer(Connection conn, Scanner input) throws SQLException {
		// get all registration info from customer
		String name, address, phone, email = "";
		input.nextLine();
		System.out.println("Please enter your name:");
		name = input.nextLine();
		System.out.println("Please enter your address:");
		address = input.nextLine();
		System.out.println("Please enter your phone number:");
		phone = input.nextLine();
		System.out.println("Please enter your email address:");
		email = input.nextLine();
		// input.close();

		System.out.println("Name:" + name);
		System.out.println("Address:" + address);
		System.out.println("Phone:" + phone);
		System.out.println("Email:" + email);
		// check last customer ID to create next primary customer ID
		Statement statement = conn.createStatement();
		ResultSet res = statement.executeQuery("SELECT max(CID) from CUSTOMER");
		int CID = res.getInt(1);
		// increment CID to create new CID
		CID++;
		String create = "(" + CID + ",'" + name + "','" + address + "','" + phone + "','" + email + "');";
		statement.executeUpdate("INSERT into CUSTOMER values" + create);
		System.out.println("Customer successfuly created, your customer ID is: " + CID
				+ ". Please remember your ID when making a reservation.");
		res.close();
		statement.close();
		return;
	}

	public static String viewHotels(Connection conn, Scanner input) throws SQLException {
		String country, state = "";
		input.nextLine().toUpperCase();
		ResultSet res = null;
		System.out.println("Please enter the country to search for a Hotel");
		country = input.nextLine().toUpperCase();
		System.out.println("Please enter the state in " + country + " to search for a Hotel");
		state = input.nextLine().toUpperCase();

		Statement statement = conn.createStatement();
		res = statement
				.executeQuery("SELECT * from HOTEL where Country ='" + country + "' AND State ='" + state + "';");
		if (!res.isBeforeFirst()) {
			System.out.println("Oops! There are no hotels present in " + state + " , " + country + "!");
			res.close();
			statement.close();
			return "noResults";
		} else {
			System.out.println("Hotels found in " + state + " , " + country + "! Below are the results:");
			System.out.println();
			while (res.next()) {
				System.out.print("Hotel #:");
				for (int i = 1; i < 6; i++) {
					System.out.print(" " + res.getString(i));
				}
				System.out.println();
			}
			System.out.println();
			System.out.println("These are the available Hotels in " + state + ", " + country + ".");
			res.close();
			statement.close();
			return "resultsFound";
		}

	}

	public static String convertDate(String date) {
		if (date.charAt(0) == '0')
			date = date.substring(1);
		for (int i = 0; i < date.length(); i++) {
			if (date.charAt(i) == '/') {
				date = date.substring(0, i) + date.substring(i + 1);
			}
		}
		return date;
	}

}
