public class Membership {
    private Customer member;
    private int points;

    public Membership(Customer member) {
        this.member = member;
        this.points = 0;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public boolean redeemPoints(int pointsToRedeem) {
        if (points >= pointsToRedeem) {
            points -= pointsToRedeem;
            return true;
        }
        return false;
    }

    public int getPoints() {
        return points;
    }
}