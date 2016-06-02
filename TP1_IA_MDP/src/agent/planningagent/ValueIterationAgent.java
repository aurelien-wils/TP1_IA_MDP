package agent.planningagent;

import java.util.List;
import java.util.Random;

import environnement.Action;
import environnement.Etat;
import environnement.MDP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ew Cet agent met a jour sa fonction de valeur avec value iteration et choisit
 * ses actions selon la politique calculee.
 *
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent {

    /**
     * discount facteur
     */
    protected double gamma;
    //*** VOTRE CODE
    protected HashMap<Etat, Double> map = new HashMap<>();

    /**
     *
     * @param gamma
     * @param mdp
     * @param map
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);
        this.gamma = gamma;
        //*** VOTRE CODE

    }

    public ValueIterationAgent(MDP mdp) {
        this(0.9, mdp);

    }

    /**
     *
     * Mise a jour de V: effectue UNE iteration de value iteration
     */
    @Override
    public void updateV() {
        //delta est utilise pour detecter la convergence de l'algorithme
        //lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
        //delta < epsilon 
        this.delta = 0.0;
//        //*** VOTRE CODE
        try {
            HashMap<Etat, Double> cloneValues = (HashMap<Etat, Double>) this.map.clone();
            List<Etat> etats = this.mdp.getEtatsAccessibles();
            for (Etat e : etats) {
                List<Action> actions = this.mdp.getActionsPossibles(e);
                Double maxA = null;
                for (Action a : actions) {
                    double somme = this.getSomme(e, a);
                    if (maxA == null || somme > maxA) {
                        maxA = somme;
                    }
                }
                if (maxA != null) {
                    cloneValues.put(e, maxA);
                }
            }
            Double maxDelta = null;
            for (Etat _e : this.map.keySet()) {
                double diff = Math.abs(this.getValeur(_e) - cloneValues.get(_e));
                if (maxDelta == null || diff > maxDelta) {
                    maxDelta = diff;
                }
            }
            if (maxDelta == null) {
                this.delta = 0.0;
            } else {
                this.delta = maxDelta;
            }
            this.map.putAll(cloneValues);
            this.notifyObs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
        // ...
        //******************* a laisser a la fin de la methode
        this.notifyObs();
    }

    public double getSomme(Etat e, Action a) throws Exception {
        Map<Etat, Double> etats = this.mdp.getEtatTransitionProba(e, a);
        double somme = 0;
        for (Etat _e : etats.keySet()) {
            double T = etats.get(_e);
            double R = this.mdp.getRecompense(e, a, _e);
            somme += T * (R + this.gamma * this.getValeur(_e));
        }
        return somme;
    }

    /**
     * renvoi l'action executee par l'agent dans l'etat e Si aucune actions
     * possibles, renvoi NONE ou null.
     */
    @Override
    public Action getAction(Etat e) {
        //*** VOTRE CODE
        List<Action> liste = this.getPolitique(e);
        if (liste.size() > 0) {
            return liste.get(new Random().nextInt(liste.size()));
        }
        return null;

    }

    @Override
    public double getValeur(Etat _e) {
        //*** VOTRE CODE
        return this.map.get(_e) == null ? 0 : this.map.get(_e);
    }

    /**
     * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
     * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si
     * aucune action n'est possible)
     */
    @Override
    public List<Action> getPolitique(Etat _e) {
        List<Action> r = new ArrayList<>();

        double max_value = -1;

        for (Action a : mdp.getActionsPossibles(_e)) {
            double value = 0.0;
            try {
                value = getSomme(_e, a);
            } catch (Exception ex) {
            }
            if (value >= max_value) {
                // Meilleure valeur
                if (value > max_value) {
                    r.clear();
                }
                r.add(a);
                max_value = value;
            }
        }

        return r;

    }

    @Override
    public void reset() {
        super.reset();
        this.map.clear();
        //*** VOTRE CODE

        /*-----------------*/
        this.notifyObs();

    }

    @Override
    public void setGamma(double arg0) {
        this.gamma = arg0;
    }

}
