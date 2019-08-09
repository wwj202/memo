
List<Map<String, Object>> listResult = new ArrayList<>();
listResult.sort((map1, map2) -> {
            Date date1 = (Date) map1.get("time");
            Date date2 = (Date) map2.get("time");
            return date1.compareTo(date2);
        });
        
        
