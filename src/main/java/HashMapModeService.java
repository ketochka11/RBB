import java.util.Currency;
import java.util.HashMap;
import java.util.Map;


public class HashMapModeService implements ModeService {
    private final Map<Long, Currency> originalCurrency = new HashMap<>();
    private final Map<Long, Currency> targetCurrency = new HashMap<>();

    public HashMapModeService() {
        System.out.println("Hashmap bot is created");
    }

    @Override
    public Currency getOriginalCurrency(long chatId) {
        return originalCurrency.getOrDefault(chatId, Currency.USD);
    }

    @Override
    public Currency getTargetCurrency(long chatId) {
        return targetCurrency.getOrDefault(chatId, Currency.USD);
    }

    @Override
    public Currency setOriginalCurrency(long chatId, Currency currency) {
        return originalCurrency.put(chatId, currency);
    }

    @Override
    public Currency setTargetCurrency(long chatId, Currency currency) {
        return targetCurrency.put(chatId, currency);
    }
}
