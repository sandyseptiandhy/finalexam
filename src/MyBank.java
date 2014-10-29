

import id.web.skyforce.bank.model.Account;
import id.web.skyforce.bank.model.Address;
import id.web.skyforce.bank.model.Customer;
import id.web.skyforce.bank.util.HibernateUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class MyBank {

	public static void main(String[] args) throws ParseException{
		int number = 0;
		int cusNumber = 0;
		int accNumber = 0;
		int addNumber = 0;
		Scanner input = new Scanner(System.in);
		
		while(number!=4){
			System.out.println("1. Customer");
			System.out.println("2. Account");
			System.out.println("3. Address");
			System.out.println("4. Keluar");
			System.out.print("Masukan pilihan: ");
			number = input.nextInt();
			
			if(number==1){
				
				while(cusNumber!=4){
				System.out.println("Menu Customer");
				System.out.println("====================");
				System.out.println("1. Tambah Customer");
				System.out.println("2. Hapus Customer");
				System.out.println("3. Lihat Daftar Customer");
				System.out.println("4. Selesai");
				System.out.println("Masukan pilihan anda");
				cusNumber = input.nextInt();
				
				if(cusNumber==1){
					tambahCustomer();
					
				} else if(cusNumber==2){
					
				} else if(cusNumber==3){
					
					Session session = HibernateUtil.openSession();
					Transaction trx = session.beginTransaction();
					Query query = session.createQuery("from Customer");
					List<Customer> listCus = query.list();
					
			
					for (Customer cust : listCus) {
						
						System.out.println("- First Name: "+cust.getFirstName());
						System.out.println("- Last Name: "+cust.getLastName());
						System.out.println("- Gender: "+cust.getGender());
						System.out.println("- Birth date: "+cust.getBirthDate());
						System.out.println("- ID Number: "+cust.getIdNumber());

					}
					System.out.println();
					trx.commit();
					session.close();
					
				}
				}
				

			}else if(number==2){
				
				while(accNumber!=4){
				System.out.println("Menu Account");
				System.out.println("====================");
				System.out.println("1. Tambah Account by id customer");
				System.out.println("2. Hapus Account");
				System.out.println("3. Lihat Daftar Account");
				System.out.println("4. Selesai");
				System.out.println("Masukan pilihan anda");
				accNumber = input.nextInt();
				
				if(accNumber==1){
					tambahAccount();
					
				} else if(accNumber==2){
					
				} else if(accNumber==3){
					
				}
				}

			}else if(number==3){
				
				while(addNumber!=4){
				System.out.println("Menu Address");
				System.out.println("====================");
				System.out.println("1. Tambah Address by id customer");
				System.out.println("2. Hapus Address");
				System.out.println("3. Lihat Daftar Address");
				System.out.println("4. Selesai");
				System.out.println("Masukan pilihan anda");
				addNumber = input.nextInt();
				if(addNumber==1){
					tambahAddress();
				} else if(addNumber==2){
					
				} else if(addNumber==3){
					
				}
				}
				
				

			}
		}

	}
	
	public static void tambahCustomer() throws ParseException{
		Session session = HibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		
		Customer cus = new Customer();
		Scanner input = new Scanner(System.in);
		
		System.out.print("Masukan nama depan: ");
		String namaDepan = input.nextLine(); 
		System.out.print("Masukan nama belakang: ");
		String namaBelakang = input.nextLine();
		System.out.print("Masukan gender: ");
		String gender = input.nextLine();
		System.out.print("Masukan tanggal dengan format (dd-MM-yyyy): ");
		String tanggal = input.nextLine();
		System.out.print("Masukan id number: ");
		String idNumber = input.nextLine();
	
		
		SimpleDateFormat formatter =  new SimpleDateFormat("dd-MM-yyyy");
		String tanggalPars = tanggal;
		Date tanggalParsing = formatter.parse(tanggalPars);
		
		cus.setFirstName(namaDepan);
		cus.setLastName(namaBelakang);
		cus.setGender(gender);
		cus.setBirthDate(tanggalParsing);
		cus.setIdNumber(idNumber);
		

		session.save(cus);
		trx.commit();
		session.close();
		
	}
	
	public static void tambahAccount(){
		Session session = HibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		
		Account acc = new Account();
		Scanner input = new Scanner(System.in);
		
		System.out.print("Masukan customer Id: ");
		String customerId = input.nextLine(); 
		Long id = Long.parseLong(customerId);
		System.out.print("Masukan nomor akun: ");
		String numberAcc = input.nextLine();
		System.out.print("Masukan nilai balance: ");
		String balance = input.nextLine();
		Double a = Double.parseDouble(balance);
		BigDecimal balanceParse = BigDecimal.valueOf(a);
		System.out.print("Masukan status (A or I or B): ");
		String status = input.nextLine();
		
		Customer cus = (Customer) session.get(Customer.class, id);
		acc.setAccountNumber(numberAcc);
		acc.setBalance(balanceParse);
		acc.setStatus(status);
		acc.setCustomer(cus);
		
		session.save(acc);
		trx.commit();
		session.close();
		
		
		
	}
	
	public static void tambahAddress(){
		Session session = HibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		
		Address add = new Address();
		Scanner input = new Scanner(System.in);
		
		System.out.print("Masukan customer Id: ");
		String customerId = input.nextLine(); 
		Long id = Long.parseLong(customerId);
		System.out.print("Masukan nama jalan: ");
		String street = input.nextLine();
		System.out.print("Masukan kota: ");
		String city = input.nextLine();
		System.out.print("Masukan kode pos: ");
		String postalCode = input.nextLine();
		Customer cus = (Customer) session.get(Customer.class, id);
		
		add.setStreet(street);
		add.setCity(city);
		add.setPostalCode(postalCode);
		add.setCustomer(cus);
		
		session.save(add);
		trx.commit();
		session.close();
		
		
	}

}
