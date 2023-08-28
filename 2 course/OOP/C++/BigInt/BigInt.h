#pragma once

class BigInt {
private:
	std::string _value;
	bool _sign;

	std::string decToBin(BigInt&) const;
	BigInt binToDec(std::string&) const;
	void addingZeroes(std::string&, size_t amt) const;

public:
	BigInt(); //��������� �������� ����
	BigInt(int); // ����������� 
	BigInt(std::string); // ������� ���������� std::invalid_argument ��� ������
	BigInt(const char*); // ������� ����������� ��� ������� char
	BigInt(const BigInt&); // ����������� �����������
	~BigInt(); // �������������

	BigInt& operator=(const BigInt&);  //�������� ������������ ������ ����!

	BigInt operator~() const;

	BigInt& operator++(); // ���������� ���������
	const BigInt operator++(int); // ����������� ���������
	BigInt& operator--(); // ���������� ���������
	const BigInt operator--(int); // ����������� ���������

	BigInt& operator+=(const BigInt&);
	BigInt& operator*=(const BigInt&);
	BigInt& operator-=(const BigInt&);
	BigInt& operator/=(const BigInt&);

	BigInt& operator^=(const BigInt&);
	BigInt& operator%=(const BigInt&);
	BigInt& operator&=(const BigInt&);
	BigInt& operator|=(const BigInt&);

	BigInt operator+() const;  // unary +
	BigInt operator-() const;  // unary -

	bool operator==(const BigInt&) const;
	bool operator!=(const BigInt&) const;
	bool operator<(const BigInt&) const;
	bool operator>(const BigInt&) const;
	bool operator<=(const BigInt&) const;
	bool operator>=(const BigInt&) const;

	operator int() const;
	operator std::string() const;

	size_t Size() const;  // size in bytes
};

BigInt operator+(const BigInt&, const BigInt&);
BigInt operator-(const BigInt&, const BigInt&);
BigInt operator*(const BigInt&, const BigInt&);
BigInt operator/(const BigInt&, const BigInt&);
BigInt operator^(const BigInt&, const BigInt&);
BigInt operator%(const BigInt&, const BigInt&);
BigInt operator&(const BigInt&, const BigInt&);
BigInt operator|(const BigInt&, const BigInt&);


std::ostream& operator<<(std::ostream& out, const BigInt& bignum);
