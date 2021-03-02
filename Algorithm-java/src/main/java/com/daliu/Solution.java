package com.daliu;

import java.util.*;

public class Solution {
    // https://leetcode-cn.com/problems/group-anagrams/solution/liang-chong-fang-shi-jie-jue-by-sdwwld/
    public static void main(String[] args) {
        Solution solution = new Solution();
        String[] strs = {"eat","tea","tan","ate","nat","bat"};
        for (List<String> strList : solution.groupAnagrams_2(strs)) {
            if (strList.size() > 0) {
                System.out.print(strList);
            }
        }
        // [eat, tea, ate][bat][tan, nat]
    }

    //https://leetcode-cn.com/problems/group-anagrams/
    //sort first
//    public List<List<String>> groupAnagrams_1(String[] strs) {
//        if (strs == null || strs.length == 0) {
//            return new ArrayList<>();
//        }
//        // map中key存储的是字符串中字母排序后新的字符串
//        Map<String, List<String>> map = new HashMap<>();
//        for (int i = 0; i < strs.length; i++) {
//            char[] c = strs[i].toCharArray();
//            Arrays.sort(c);
//            String key = String.valueOf(c);
//            if (!map.containsKey(key)) {
//                map.put(key, new ArrayList<>());
//            }
//            map.get(key).add(strs[i]);
//        }
//        return new ArrayList<>(map.values()); // 8ms
//    }


    public List<List<String>> groupAnagrams_2(String[] strs) {
        if (strs == null || strs.length == 0)
            return new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] cArr = new char[26];
            for (char c : s.toCharArray()) {
                cArr[c - 97]++; // cArr[c - 'a']++;
            }
            String key = String.valueOf(cArr);
            if (!map.containsKey(key))
                map.put(key, new ArrayList<>());
            map.get(key).add(s);
        }
        return new ArrayList<>(map.values());
    }
}
