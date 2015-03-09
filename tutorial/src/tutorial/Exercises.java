package tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static tutorial.infra.MissingImplementation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

/**
 * This test class contains a few cases that are currently marked
 * with {@link Ignore @Ignore}. Remove this tag, implement the test with
 * the Java8 lambda and stream API and run it as a JUnit test.
 * 
 * Some of the exercises have been adopted from the excellent tutorial
 * by Simon Ritter as given at EclipseCon Europe 2014.
 */
@SuppressWarnings("unused")
public class Exercises {

	// The first exercises aim at making you familiar with the
	// lambda syntax. Some of them will actually mutate the input
	// data. Later exercises aim at pure operations that do not
	// change any data but transform it instead.
	
	/**
	 * Combine the numbers to a string.
	 */
	@Test
	public void combineNumbersToString() {
		final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6);

		final StringBuilder result = new StringBuilder();
		input.forEach(result::append);
		
		assertEquals("123456", result.toString());
	}

	/*
	 * Explore default methods on Iterable, e.g. #forEach or #stream
	 */

	/**
	 * Remove the numbers that are divisible by 3.
	 */
	@Test
	public void removeNumbersDivisibleBy3() {
		final List<Integer> input = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

		input.removeIf(i->i%3 == 0);

		assertEquals("[1, 2, 4, 5]", input.toString());
	}

	/*
	 * Mutate the list in-place by conditionally removing elements. Explore
	 * Collection#removeIf
	 */

	/**
	 * Replace every number by its value times 10.
	 */
	@Test
	public void multiplyBy10() {
		final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6);

		input.replaceAll(i->i*10);

		assertEquals("[10, 20, 30, 40, 50, 60]", input.toString());
	}

	/*
	 * Hint: Use List.replaceAll().
	 */

	/**
	 * Convert every key-value pair of a map into a string and append them all
	 * into a single string, in iteration order.
	 */
	@Test
	public void mapToString() {
		final Map<Integer, Integer> input = new TreeMap<>();
		input.put(3, 4);
		input.put(1, 2);
		input.put(5, 6);

		final StringBuilder result = new StringBuilder();
		input.forEach((k, v)->result.append(k).append(v));
		
		assertEquals("123456", result.toString());
	}

	/*
	 * Explore the default methods on Map, e.g. #forEach
	 */

	/**
	 * Given a list of numbers, create a map whose key is a boolean if the number
	 * is even (true) or odd (false) and the value is the sum of all even or odd numbers in the list.
	 */
	@Test
	public void mapOfSums() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
		final Map<Boolean, Integer> result = new TreeMap<>();

		list.forEach(i->result.merge(i%2==0, i, (v, n)->v+n));

		assertEquals("{false=9, true=12}", result.toString());
	}

	/*
	 * Hint: Use Map.merge() within Iterable.forEach().
	 */

	// The following exercises utilize the stream API do do transformations
	// rather than mutations.

	/**
	 * Given a list of numbers, create an output list that contains the doubled
	 * values of all even numbers in the input list.
	 */
	@Test
	public void doubleEvenNumbers() {
		final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6);

		final List<Integer> result = input.stream().filter(i -> i % 2 == 0)
				.map(i -> i * 2).collect(Collectors.toList());

		assertEquals("[4, 8, 12]", result.toString());
	}

	/*
	 * Hint 1: Use filter() and map().
	 */
	/*
	 * Hint 2: Use Use collect() to create the result list. See also the java.util.Collectors
	 */

	/**
	 * Join the value times 20 of the numbers 2 to 4 in the list (inclusive,
	 * counting from zero), separated by commas, into a single string.
	 */
	@Test
	public void joinStreamRange() {
		final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6);

		final String result = input.stream().skip(1).limit(3).map(i -> i * 20)
				.map(String::valueOf).collect(Collectors.joining(","));

		assertEquals("40,60,80", result);
	}

	/*
	 * Hint 1: Use Stream.skip() and Stream.limit().
	 */
	/*
	 * Hint 2: Use Use Collectors.joining().
	 */

	/**
	 * Count the number of prime values in the list.
	 * 
	 * Bonus points for functional answer to the question "Is this a prime number?".
	 */
	@Test
	public void filterAndCount() throws IOException {
		final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		final long primes = input.stream().filter(this::isPrime).count();

		assertEquals(4, primes);
	}
	
	private boolean isPrime(int p) {
		if (p < 2) {
			return false;
		}
		return IntStream.rangeClosed(2, (int) Math.sqrt(p)).noneMatch(i->p%i == 0);
	}

	/*
	 * Hint: Use Use Stream.count().
	 */

	/**
	 * Implement the fizz buzz test.
	 * 
	 * For the first 100 positive integers, print the word 'fizz'
	 * for each number divisible by 3, and 'buzz' for each number divisible
	 * by 5. If the number is divisible
	 * by 3 and 5, print 'fizzbuzz'. Concatenate to a string delimited by comma.
	 */
	@Test
	public void fizzbuzz() throws IOException {
		final IntStream allIntegers = IntStream.iterate(1, i->i+1);
		
		final String result = allIntegers.limit(100).mapToObj(this::fizzbuzz)
				.filter(s -> s != null).collect(Collectors.joining(","));
		
		int length = result.length();
		assertEquals(258, length);
	}

	private String fizzbuzz(int i) {
		if (i%3 == 0) {
			if (i%5==0) {
				return "fizzbuzz";
			}
			return "fizz";
		}
		if (i%5 == 0) {
			return "buzz";
		}
		return null;
	}

	/*
	 * Hint 1: Use IntStream.mapToObj() to convert to Stream.
	 */
	/*
	 * Hint 2: Use IntStream.limit() to limit the size of the stream.
	 */

	/**
	 * Collect the Fibunacci numbers between 100 and 1000 (incl).
	 * 
	 * A Fibunacci number is a number that is the sum of its two predecessor
	 * Fibunacci numbers, e.g. the n'th Fibunacci number is 
	 * {@code fib(n) = fib(n-1) + fib(n-2)}.
	 * 
	 * The Fibunacci sequence starts with 1, 2, 3, 5.
	 */
	@Test
	public void countFibunacciNumbers() throws IOException {
		IntStream stream = fibunacciSequence();
		List<Integer> fibs = stream.limit(1000)
				.filter(i -> i >= 100 && i <= 1000).boxed()
				.collect(Collectors.toList());
		assertEquals("[144, 233, 377, 610, 987]", fibs.toString());
	}

	private IntStream fibunacciSequence() {
		return IntStream.iterate(1, new IntUnaryOperator() {
			int current = 1;
			@Override
			public int applyAsInt(int operand) {
				int result = current + operand;
				current = operand;
				return result;
			}
		});
	}

	/*
	 * Hint: Don't forget about anonymous classes if you need state in the stream building.
	 */

	/**
	 * Find the biggest number below 10.000 that is the product of 
	 * two distinct prime numbers.
	 * 
	 * Bonus points for functional algorithm to identify a number that is a product of two primes.
	 */
	@Test
	public void biggestProductOfTwoPrimes() {
		int biggest = IntStream.iterate(10000, i->i-1).filter(this::isProductOfTwoPrimes).findFirst().getAsInt(); 
		assertEquals(9998, biggest);
	}
	
	private boolean isProductOfTwoPrimes(int p) {
		if (p < 2) {
			return false;
		}
		class DivResult {
			int div;
			int res;
			DivResult(int div, int res) {
				this.div = div;
				this.res = res;
			}
		}
		return IntStream.rangeClosed(2, (int) Math.sqrt(p))
				.filter(i->p%i == 0)
				.boxed()
				.findFirst()
					.map(div->new DivResult(div, p/div))
					.filter(d->d.div != d.res)
					.map(d->d.res)
					.filter(this::isPrime).isPresent();
	}
	
	/**
	 * Count the numbers below 500 that are the product of 
	 * two distinct prime numbers.
	 */
	@Test
	public void countProductsOfTwoPrimes() {
		long count = IntStream.range(1, 500).filter(this::isProductOfTwoPrimes).count();
		assertEquals(145, count);
	}
	
	/**
	 * Count the numbers below 1_000.000 that are the product of 
	 * two distinct prime numbers.
	 */
	@Test
	public void countProductsOfTwoPrimesAgain() {
		long count = IntStream.range(1, 1_000_000).parallel().filter(this::isProductOfTwoPrimes).count();
		assertEquals(209_867, count);
	}
	
	/*
	 * Hint: Use parallel streams to speed up the computation.
	 */
	
	/**
	 * Given two lists of Integer, compute a third list where each element is
	 * the difference between the corresponding elements of the two input lists
	 * (first minus second).
	 */
	@Test
	public void listDifference() {
		List<Integer> one = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
		List<Integer> two = Arrays.asList(2, 7, 1, 8, 2, 8, 1, 8, 2, 8);

		List<Integer> result = IntStream.range(0, one.size()).mapToObj(i->one.get(i)-two.get(i)).collect(Collectors.toList());

		assertEquals("[1, -6, 3, -7, 3, 1, 1, -2, 3, -5]", result.toString());
	}

	/*
	 * Hint 1: Run an IntStream of list positions (indexes).
	 */
	/*
	 * Hint 2: Deal with boxed Integers either by casting or by using
	 * mapToObj().
	 */

	/**
	 * Convert a list of numbers into a list of their prime factors.
	 * 
	 * Bonus points for functional implementation to obtain the list of prime numbers.
	 */
	@Test
	public void numbersToPrimeFactors() {
		List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		List<Integer> result = input.stream().flatMap(this::primeFactors).collect(Collectors.toList());

		assertEquals("[2, 3, 2, 2, 5, 2, 3, 7, 2, 2, 2, 3, 3, 2, 5]",
				result.toString());
	}

	/*
	 * Hint: Use Stream.flatMap().
	 */
	
	private Stream<Integer> primeFactors(int number) {
		if (number < 2) {
			return Stream.empty();
		}
		return IntStream
				.rangeClosed(2, number)
				.filter(i -> number % i == 0)
				.limit(1)
				.boxed()
				.flatMap(
						i -> Stream.concat(Stream.of(i), primeFactors(number / i)));
	}


	/**
	 * Convert a list of numbers into a list of prime factors and return
	 * the distinct, odd prime factors sorted descending.
	 */
	@Test
	public void oddPrimeFactorsSorted() throws IOException {
		List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

		List<Integer> result = input.stream().flatMap(this::primeFactors)
				.filter(i -> i % 2 == 1).distinct()
				.sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		assertEquals("[19, 17, 13, 11, 7, 5, 3]", result.toString());
	}

	/*
	 * Hint 1: Use Stream.sorted().
	 */
	/*
	 * Hint 2: Use Comparator.reverseOrder().
	 */

	/**
	 * Count the total number of prime factors of the numbers between 1 and 10000 (incl),
	 * the _distinct_ number of prime factors and the distinct number of prime factors,
	 * that are also Fibunacci numbers.
	 */
	@Test
	public void differentCountsAtOnce() {
		IntSummaryStatistics total = new IntSummaryStatistics();
		IntSummaryStatistics distinct = new IntSummaryStatistics();

		long fibCount = IntStream.rangeClosed(1, 10000)
				.flatMap(i -> primeFactors(i).mapToInt(Integer::intValue))
				.peek(total::accept).distinct().peek(distinct::accept)
				.filter(this::isFib).count();
		long distinctCount = distinct.getCount();
		long totalCount = total.getCount();

		assertEquals("fib count", 7, fibCount);
		assertEquals("distinct count", 1229, distinctCount);
		assertEquals("total count", 31985, totalCount);
	}
	
	private boolean isFib(int number) {
		return fibunacciSequence().filter(i->i>=number).findFirst().getAsInt() == number;
	}
	
	/*
	 * Hint 1: Use Stream.peek().
	 */
	/*
	 * Hint 2: Use LongAdder, IntSummeryStatistics or AtomicLong/AtomicInteger to allow peek() to have
	 * side effects.
	 */

	/**
	 * Categorize the numbers between 1 and 100 according to the fizzbuzz rules.
	 * Don't categorize any numbers that are neither divisible by 3 or 5.
	 */
	@Test
	public void fizzbuzzAgain() throws IOException {
		Map<String, List<Integer>> result = IntStream.rangeClosed(1, 100).boxed()
				.filter(i -> fizzbuzz(i) != null)
				.collect(Collectors.groupingBy(this::fizzbuzz));

		assertEquals(27, result.get("fizz").size());
		assertEquals(14, result.get("buzz").size());
		assertEquals(6, result.get("fizzbuzz").size());
	}

	/*
	 * Hint: Use Collectors.groupingBy().
	 */

	/**
	 * Categorize the numbers between 1 and 100 according to the fizzbuzz rules
	 * but only keep the count of the numbers that fall into each criteria. Also
	 * count the numbers that do not match the fizzbuzz rules into a category
	 * '' (the empty string).
	 */
	@Test
	public void fizzbuzzCounting() throws IOException {
		Map<String, Long> result = IntStream.rangeClosed(1, 100).boxed()
				.map(i -> Optional.ofNullable(fizzbuzz(i)).orElse(""))
				.collect(Collectors.groupingBy(s -> s, Collectors.counting()));

		assertEquals(27, result.get("fizz").longValue());
		assertEquals(14, result.get("buzz").longValue());
		assertEquals(6, result.get("fizzbuzz").longValue());
		assertEquals(53, result.get("").longValue());
		assertEquals(4, result.size());
	}

	/*
	 * Hint 1: Use the "downstream" overload of Collectors.groupingBy().
	 */
	/*
	 * Hint 2: Use Collectors.counting().
	 */

	/**
	 * Given a stream of strings, accumulate (collect) them into the result
	 * string by inserting the input string at both the beginning and end. For
	 * example, given input strings "x" and "y" the result should be "yxxy".
	 * Note: the input stream is a parallel stream, so you MUST write a proper
	 * combiner function to get the correct result.
	 */
	@Test
	public void beCarefulWithParallelStreams() {
		Stream<String> input = Arrays
				.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
						"l", "m", "n", "o", "p", "q", "r", "s", "t")
				.parallelStream();

		String result =
	            input.collect(StringBuilder::new,
                        (sb, s) -> sb.insert(0, s).append(s),
                        (sb1, sb2) -> {
                            int half = sb2.length() / 2;
                            sb1.insert(0, sb2.substring(0, half));
                            sb1.append(sb2.substring(half));
                        })
               .toString();

		assertEquals("tsrqponmlkjihgfedcbaabcdefghijklmnopqrst", result);
	}

	/*
	 * Hint 1: The combiner function must take its second argument and merge it
	 * into the first argument, mutating the first argument.
	 */
	/*
	 * Hint 2: The second argument to the combiner function happens AFTER the
	 * first argument in encounter order, so the second argument needs to be
	 * split in half and prepended/appended to the first argument.
	 */

}
