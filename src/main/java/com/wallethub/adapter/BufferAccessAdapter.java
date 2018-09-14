package com.wallethub.adapter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.common.net.InetAddresses;
import com.wallethub.domain.model.Access;
import com.wallethub.exception.JavaMysqlParserError;
import com.wallethub.exception.JavaMysqlParserException;

public class BufferAccessAdapter {
	private String myLine;
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public BufferAccessAdapter(String myLine) {
		this.myLine = myLine;
	}

	public Access invoke() throws UnknownHostException {
		String[] split = myLine.split("\\|");

		if (split.length != 5)
			throw new JavaMysqlParserException(JavaMysqlParserError.WRONG_LINE_FORMAT,
					"The line format should have 5 parameters with | delimiter");

		InetAddress IP = InetAddress.getByName(split[1]);
		Long integerAddress = getUnsignedInt(InetAddresses.coerceToInteger(IP));
		String desc = split[2];

		LocalDateTime dateTime = LocalDateTime.parse(split[0], dateFormatter);
		return Access.builder().withIp(integerAddress).withDate(dateTime).withRequestInfo(desc)
				.withResponseStatus(split[3]).withExtraInfo(split[4]).build();
	}

	public static long getUnsignedInt(int x) {
		return x & 0x00000000ffffffffL;
	}

}
