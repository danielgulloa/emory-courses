/**
 * Copyright 2014, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.cs323.select;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import edu.emory.mathcs.utils.DSUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SelectTest 
{
	@Test
	public void testAccuracy()
	{
		AbstractSelect<Integer> s1 = new DumbSelect<Integer>();
		AbstractSelect<Integer> s2 = new SmartSelect<Integer>();
		
		List<Integer> originalList = DSUtils.toArrayList(new Integer[]{4,1,3,2,5,6,8,3,4,7,5,9,7});
		List<Integer> sortedList   = new ArrayList<>(originalList);
		Collections.sort(sortedList, Collections.reverseOrder());
		
		for (int k=0; k<originalList.size(); k++)
		{
			assertEquals(sortedList.get(k), s1.max(originalList, k+1));
			assertEquals(sortedList.get(k), s2.max(originalList, k+1));
		}
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testSpeed()
	{
		testSpeed(new DumbSelect<Integer>(), new SmartSelect<Integer>());
	}
	
	@SuppressWarnings("unchecked")
	void testSpeed(final AbstractSelect<Integer>... engines)
	{
		final int MAX_K = 100, ITER = 1000, SIZE = 1000, ENGINE_LEN = engines.length;
		List<Integer> list = DSUtils.getRandomIntegerList(new Random(1), SIZE);
		long[][] times = new long[ENGINE_LEN][MAX_K];

		for (int i=0; i<ITER; i++)
			for (int j=0; j<ENGINE_LEN; j++)
				for (int k=0; k<MAX_K; k++)
					times[j][k] += getRuntime(engines[j], list, k+1);

		StringBuilder build = new StringBuilder();
		
		for (int k=0; k<MAX_K; k++)
		{
			build.append(k+1);
			
			for (int j=0; j<ENGINE_LEN; j++)
			{
				build.append("\t");
				build.append(1.0*times[j][k]/ITER);
			}
			
			build.append("\n");
		}
		
		System.out.println(build.toString());
	}
	
	long getRuntime(AbstractSelect<Integer> s, List<Integer> list, int k)
	{
		long st, et;
		
		st = System.currentTimeMillis();
		s.max(list, k);
		et = System.currentTimeMillis();
		
		return et - st;
	}
}
