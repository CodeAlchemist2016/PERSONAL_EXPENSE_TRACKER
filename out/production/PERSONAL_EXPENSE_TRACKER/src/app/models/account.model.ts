export interface Account {
  id: number;
  accountType: string;
  balance: number;
  bankName: string;
  investmentType?: string;
  portfolioValue?: number;
  accountNumber: string;
  maxSpending?: number;
  user: {user_id: number; user_name: string};
}
