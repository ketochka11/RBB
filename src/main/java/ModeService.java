import java.util.Currency;
import java.util.HashMap;

public interface ModeService {

    static ModeService getInstance() {
        return new HashMapModeService();
    }

    Currency getOriginalCurrency(long chatId);

    Currency getTargetCurrency(long chatId);

    Currency setOriginalCurrency(long chatId, Currency currency);

    Currency setTargetCurrency(long chatId, Currency currency);
}
