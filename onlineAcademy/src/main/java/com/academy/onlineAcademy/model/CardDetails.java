package com.academy.onlineAcademy.model;

import java.time.LocalDate;
import java.util.Date;

public class CardDetails {

	private int cardNumber;
	private LocalDate expiryDate;
	private int secCode;

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getSecCode() {
		return secCode;
	}

	public void setSecCode(int secCode) {
		this.secCode = secCode;
	}

	/**
	 * Parameterized class constructor.
	 * 
	 * @param cardNumber - first parameter, for the credit card number
	 * @param expiryDate - second parameter, for the expire date of the card (will
	 *                   be in mm/yy format)
	 * @param secCode    - third and last parameter, for the security code of the
	 *                   credit card
	 */
	public CardDetails(int cardNumber, LocalDate expiryDate, int secCode) {
		super();
		this.cardNumber = cardNumber;
		this.expiryDate = expiryDate;
		this.secCode = secCode;
	}

	/**
	 * Class constructor.
	 */
	public CardDetails() {

	}

}
