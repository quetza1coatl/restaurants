package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.repository.CrudRestaurantRepository;
import com.quetzalcoatl.restaurants.repository.CrudUserRepository;
import com.quetzalcoatl.restaurants.repository.CrudVotesRepository;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.quetzalcoatl.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service("votesService")
public class VotesService {

    private final CrudVotesRepository repository;
    private final CrudUserRepository userRepository;
    private final CrudRestaurantRepository restaurantRepository;

    private final LocalTime REVOTE_TIME = LocalTime.of(11,0);

    @Autowired
    public VotesService(CrudVotesRepository repository,
                        CrudUserRepository userRepository,
                        CrudRestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Votes create(int restaurantId, int userId, LocalDateTime dateTime) {
        Votes vote = new Votes();
        vote.setRestaurant(restaurantRepository.findById(restaurantId).get());
        vote.setUser(userRepository.findById(userId).get());
        vote.setDateTime(dateTime);
        return repository.save(vote);


    }

    @Transactional
    public Votes update(int voteId, int newRestaurantId, LocalDateTime dateTime) {
        Votes vote = repository.findById(voteId).orElseThrow();
        if (isLate(dateTime.toLocalTime(), dateTime.toLocalDate(), vote.getDateTime().toLocalDate())) {
            throw new LateToVoteException("It is late to change your vote");
        }
        vote.setRestaurant(restaurantRepository.getOne(newRestaurantId));
        vote.setDateTime(dateTime);
        return checkNotFoundWithId(repository.save(vote), vote.getId());

    }

    public List<Votes> getAll() {
        return repository.findAll();
    }

    public boolean isVotesOnDate(int userId, LocalDate date) {
        List<LocalDateTime> list = repository.getDateTimeByUser(userId);
        List<LocalDate> dateList = list.stream()
                .map(d -> d.toLocalDate())
                .distinct()
                .filter(d -> d.isEqual(date))
                .collect(Collectors.toList());
        return !dateList.isEmpty();

    }

    public int getVoteIdByUserAndDate(int userId, LocalDate date){
        List<Votes> votesByUser = repository.getByUserId(userId);
        return votesByUser.stream()
                .filter(v -> (v.getDateTime().toLocalDate().isEqual(date)))
                .collect(Collectors.toList())
                .get(0)
                .getId();
    }

    //vote history
    public List<Votes> getAllByRestaurantId(int id) {
        return repository.getByRestaurantId(id);
    }

    public Votes get(Integer id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    private boolean isLate(LocalTime time, LocalDate actualDate, LocalDate dateFromVote) {
        return time.isAfter(REVOTE_TIME) || !actualDate.isEqual(dateFromVote);

    }

}
