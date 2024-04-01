package com.haccercat.client.DataStream;

import java.util.Arrays;

public class ByteStream {

	public byte[] payload;
	public int offset = 0;

	public ByteStream(int size) {
		this.payload = new byte[size];
	}

	public ByteStream(byte[]  bytes) {
		this.payload = bytes;
	}

	public void write(int a1) {
		byte v1 = (byte) a1;
		this.payload[offset] = v1;
		offset += 1;
	}
	public int read() {
		int result = (this.payload[offset] & 0xFF);
		offset += 1;
		return result;
	}
	public void writeUInt(int a1) {
		this.write(a1 & 0xFF);
	}
	public void writeByte(int a1) {
		this.write(a1);
	}
	public void writeBoolean(Boolean a1) {
		this.write(a1 ? 1 : 0);
	}
	public void writeInt(int a1) {
		this.write((a1 >> 24) & 0xFF);
		this.write((a1 >> 16) & 0xFF);
		this.write((a1 >> 8) & 0xFF);
		this.write(a1 & 0xFF);
	}
	public void writeString(String a1) {
		byte[] b = a1.getBytes();
		this.writeInt(b.length);
		for (int strOffset = 0; strOffset < b.length; strOffset++) {
			this.write(b[strOffset]);
		}
	}
	public void writeString() {
		this.writeInt(-1);
	}

	public void writeVInt(int a1) {
		int v1, v2, v3;
		v1 = (((a1 >> 25) & 0x40) | (a1 & 0x3F));
		v2 = ((a1 ^ (a1 >> 31)) >> 6);
		a1 >>= 6;
		if (v2 == 0) {
			this.writeByte(v1);
		} else {
			this.writeByte(v1 | 0x80);
			while (v2 != 0) {
				v2 >>= 7;
				v3 = 0;
				if (v2 > 0) {
					v3 = 0x80;
				}
				this.writeByte((a1 & 0x7F) | v3);
				a1 >>= 7;
			}
		}
	}

	public void writeDataReference(int a1) {
		writeVInt(a1);
	}

	public void writeDataReference(int a1, int a2) {
		this.writeVInt(a1);
		this.writeVInt(a2);
	}
	public int readInt() {
		return (this.read() << 24 | this.read() << 16 | this.read() << 8 | this.read());
	}
	public byte readByte() {
		return (byte) this.read();
	}
	public byte[] readBytes(int size) {
		byte[] result = new byte[size];
		for (var index = 0; index < size; index++) {
			result[index] = this.readByte();
		}
		return result;
	}
	public Boolean readBoolean() {
		Boolean result = false;
		if (this.read() >= 1) {
			result = true;
		}
		return result;
	}
	public String readString() {
		String result = "";
		int len = this.readInt();
		if (len <= 0) {
			return "";
		}
		result = new String(this.readBytes(len));
		return result;
	}
	public int readVInt() {
	    // this method is discovered by nameless#1347
		int result = 0, shift = 0, b, seventh, msb, n;
		while (true) {
			b = this.read();
			if (shift == 0) {
				seventh = (b & 0x40) >> 6;
				msb = (b & 0x80) >> 7;
				n = b << 1;
				n = n & ~0x181;
				b = n | (msb << 7) | seventh;
			}
			result |= (b & 0x7f) << shift;
			shift += 7;
			if ((b & 0x80) <= 0) {
				break;
			}
		}
		return (result >> 1) ^ (-(result & 1));
	}
	public byte[] getBytes() {
		return Arrays.copyOfRange(this.payload, 0, this.offset);
	}
}
