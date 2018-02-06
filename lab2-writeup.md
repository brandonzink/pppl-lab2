Cam Connor and Brandon Zink
Lab 2 Writeup

2a. S ::= A B A will generate
		One or more a's followed by either nothing or one or more b's followed by a c (since epsilon is considered empty), followed by 1 or more a's.

		L(g) ::= {a*n, ε | (b*n, c), a*n : n>0}

2b. Sentences 1 and 4
		1. baab: S => AaBb => baBb => baab
		4. bbaab: S=> AaBb => AbaBb => bbaBb => bbaab


2c. Sentences 1 and 5
		1.			S
				/  / \  \
				a  S  c  B
				   |     |
				   b     d

		5.			S
				/  / \  \
				a  S  c  B
				   |     |
				   A     D
				   |	 |
				   c     c

2d. You can get the output "a" via two ways:
	A => a
	A => A⊕A => b⊕(A⊕A) => b⊕(a⊕b) => a

2e. You can only arrive at one a total due to the fact that the A⊕A will always result in either nothing, a, or b due to the fact that repeating a or b would cancel out.

3ai. The first is left associative due to the fact that the call of e is on the right side, thus the recursive non-terminal symbol is on the left. The second is right associative due to the fact that the the recurisve non-terminal symbol is on the right of the tree.

3aii. The same, just by oppositive associations. They will produce operand operator operand operator .. operand, where the difference is determined based on the addition of an operand on the left or right.

3b. Example: 6 - 2 << 2
			println((6-2) << 2)
			println(6 - (2<<2))
			println(6 - 2 << 2)

		We can compare the outputs to see if the output on line 3 is equivalent to line 1 or line 2. By running this we discover that - has a higher precedance than <<.

3c. 
	num ::= -a.a|a.a|b.a|-b.a|-a.b|a.b|exp|b.b
	exp :: = b.aEb|-b.aEa|a.aEa|-a.aEa|a.bEa|-a.bEa
	a ::= 1|2|3|4|...|inf
	b ::= 0